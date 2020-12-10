/**
 * Copyright (c) 2020-present Link Development
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkdev.dragdismiss.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.linkdev.dragdismiss.R
import com.linkdev.dragdismiss.models.DragDismissDefaults
import com.linkdev.dragdismiss.models.DragDismissDirections
import com.linkdev.dragdismiss.models.DragDismissVelocityLevel
import com.linkdev.dragdismiss.utils.*
import kotlin.math.abs
import kotlin.properties.Delegates

/**
 *  There are two ways to dismiss a DragDismissLayout
 *
 * - [mDragDismissVelocity] If the view is flanged above a certain speed.
 *
 * - [mDragDismissScreenPercentage] If the layout traveled a certain percentage of the screen.
 *
 * Created on 5/6/2018.
 */
internal class DragDismissLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    /**
     * The starting visibility of the canvas background for the dismiss background fade out effect.
     *
     * @default [DragDismissDefaults.DEFAULT_BACKGROUND_DIM]
     */
    private var mBackgroundDim =
        Utilities.calculateAlphaFromFraction(DragDismissDefaults.DEFAULT_BACKGROUND_DIM)

    /**
     * The distance traveled before the view initiate a dismiss on finger up.
     *
     * So if it's set to [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE] then if the layout is dragged more than [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE] of the screen the view will be dismissed on release.
     *
     * @default [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE]
     * @see mDragDismissVelocity
     */
    private var mDragDismissScreenPercentage = DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE

    /**
     * There are two implemented ways to dismiss the view
     *
     * 1) [mDragDismissVelocity] If the view is flanged above a certain speed.
     *
     * 2) [mDragDismissScreenPercentage] If the layout passed a certain percentage of the screen.
     *
     * So if it's set to [DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL] then if the layout is dragged at speed pass [DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL] will be dismissed on release.
     *
     * @default [DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL]
     * @see mDragDismissScreenPercentage
     */
    private var mDragDismissVelocity = DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL.velocity

    /**
     * The first child of the DragDismissLayout, and the view to be dragged.
     */
    private lateinit var mDraggedView: View

    /**
     * The selected drag directions(Can be more than one direction) from [DragDismissDirections]
     *
     *
     * @default [DragDismissDefaults.DEFAULT_DRAG_DIRECTION]
     */
    private lateinit var mSelectedDragBackDirections: DragDismissDirections

    /**
     * Pointers to the current touch event start points.
     */
    private var mPointerX by Delegates.notNull<Float>()
    private var mPointerY by Delegates.notNull<Float>()

    /**
     * The Scrollable views inside the DragDismissLayout
     */
    private var mInnerScrollViewsList = arrayListOf<View>()

    /**
     * The fraction or percentage of the current position to the view dimension (top or left which ever moved most).
     */
    private var mSwipeBackFraction = 0.0f

    /**
     * The minimum distance in pixels that the user must travel to initiate a drag.
     */
    private var mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private var mWidth = 0
    private var mHeight = 0
    private var mTopOffset = paddingTop
    private var mLeftOffset = paddingLeft
    private var mDragHelper = ViewDragHelper.create(this, 1f, DragHelperCallback())
    private var mDismiss: Boolean = false

    /**
     * The callback method after the animation is done the implementing activity will have to call it's finish
     */
    private var mDismissCallback: (() -> Unit)? = null

    init {
        setWillNotDraw(false)
        initDragHelper()

        initAttrs(context, attrs)
    }

    private fun initDragHelper() {
        mDragHelper = ViewDragHelper.create(this, 1f, DragHelperCallback())
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragDismissLayout)

        val directionFlag =
            typedArray.getInt(
                R.styleable.DragDismissLayout_draggingDirections,
                DragDismissDefaults.DEFAULT_DRAG_DIRECTION.value
            )
        mSelectedDragBackDirections =
            DragDismissDirections[directionFlag] ?: DragDismissDefaults.DEFAULT_DRAG_DIRECTION

        mDragDismissScreenPercentage =
            typedArray.getFraction(
                R.styleable.DragDismissLayout_dragDismissScreenPercentage,
                1,
                1,
                DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE
            )

        mBackgroundDim =
            Utilities.calculateAlphaFromFraction(
                typedArray.getFraction(
                    R.styleable.DragDismissLayout_backgroundDim,
                    1,
                    1,
                    DragDismissDefaults.DEFAULT_BACKGROUND_DIM
                )
            )

        mDragDismissVelocity = typedArray.getInt(
            R.styleable.DragDismissLayout_dragDismissVelocityLevel,
            DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL.velocity
        )

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        check(childCount <= 1) { "DragDismissLayout should contain only one direct child." }

        var childMeasuredWidth = 0
        var childMeasuredHeight = 0
        if (childCount == 1) {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            mDraggedView = getChildAt(0)
            childMeasuredWidth = mDraggedView.measuredWidth
            childMeasuredHeight = mDraggedView.measuredHeight
        }

        val measuredWidth = View.resolveSize(
            childMeasuredWidth + paddingLeft + paddingRight,
            widthMeasureSpec
        )
        val measuredHeight = View.resolveSize(
            childMeasuredHeight + paddingTop + paddingBottom,
            heightMeasureSpec
        )

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) return

        check(childCount <= 1) { "DragDismissLayout should contain only one direct child." }

        mDraggedView = getChildAt(0)

        val left = paddingLeft + mLeftOffset
        val top = paddingTop + mTopOffset
        val right = left + mDraggedView.measuredWidth
        val bottom = top + mDraggedView.measuredHeight
        mDraggedView.layout(left, top, right, bottom)

        if (changed) {
            mWidth = width
            mHeight = height
        }

        mInnerScrollViewsList = Utilities.findAllScrollViews(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Sets the alpha of the canvas as the view position changes
        canvas.drawARGB(
            (mBackgroundDim - (mBackgroundDim * mSwipeBackFraction)).toInt(),
            0,
            0,
            0
        )
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPointerX = event.rawX
                mPointerY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                // Handle children scrollable views touch interception.
                if (mInnerScrollViewsList.isNotEmpty()) {
                    val xOffset = abs(event.rawX - mPointerX)
                    val yOffset = abs(event.rawY - mPointerY)
                    if (xOffset < mTouchSlop && yOffset < mTouchSlop)
                        return false

                    for (innerScrollView in mInnerScrollViewsList) {
                        if (innerScrollView.contains(mPointerX, mPointerY)) {
                            var shouldIntercept = true
                            if (selectedLeftDragBack() || selectedRightDragBack()) {
                                shouldIntercept = !(yOffset > mTouchSlop && yOffset > xOffset)
                            }
                            if (!shouldIntercept && (selectedTopDragBack() || selectedBottomDragBack())) {
                                shouldIntercept = !(xOffset > mTouchSlop && xOffset > yOffset)
                            }
                            return if (shouldIntercept)
                                super.onInterceptTouchEvent(event) ||
                                        mDragHelper.shouldInterceptTouchEvent(event)
                            else super.onInterceptTouchEvent(event)
                        }
                    }
                }
            }
        }
        return mDragHelper.shouldInterceptTouchEvent(event) || super.onInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }

    /**
     * Needed for automatic settling, When calling [ViewDragHelper.settleCapturedViewAt].
     */
    override fun computeScroll() {
        if (mDragHelper.continueSettling(true))
            ViewCompat.postInvalidateOnAnimation(this)
    }

    inner class DragHelperCallback : ViewDragHelper.Callback() {
        private var mIsDraggedFromLeft = false
        private var mIsDraggedFromRight = false
        private var mIsDraggedFromTop = false
        private var mIsDraggedFromBottom = false

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === mDraggedView
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            // Clamp the view if it's not All Drag and not dragged from left and not dragged from right
            if (!selectedLeftDragBack() && !selectedRightDragBack())
                return paddingLeft

            mLeftOffset = paddingLeft

            val shouldClamp =
                if (mInnerScrollViewsList.isEmpty()) false
                else shouldClampHorizontalScrollViews(left)

            return if (shouldClamp) paddingLeft
            else {
                // Move the view by updating it's leftOffset
                if (selectedLeftDragBack() && left > 0)
                    onDragFromLeft(left)
                else if (selectedRightDragBack() && left < 0)
                    onDragFromRight(left)
                mLeftOffset
            }
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            // Clamp the view if it's not All Drag and not dragged from top and not dragged from bottom
            if (!selectedBottomDragBack() && !selectedTopDragBack())
                return paddingTop

            mTopOffset = paddingTop

            val shouldClamp =
                if (mInnerScrollViewsList.isEmpty()) false
                else shouldClampVerticalScrollViews(top)

            return if (shouldClamp) paddingTop
            else {
                // Move the view by updating it's leftOffset
                if (selectedTopDragBack() && top > 0)
                    onDragFromTop(top)
                else if (selectedBottomDragBack() && top < 0)
                    onDragFromBottom(top)
                mTopOffset
            }
        }

        override fun onViewPositionChanged(view: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(view, left, top, dx, dy)
            val absLeft = abs(left)
            val absTop = abs(top)

            // check which direction was moved most to determine the moved fraction
            mSwipeBackFraction =
                if (absTop > absLeft) absTop.toFloat() / mHeight
                else absLeft.toFloat() / mWidth
            invalidate()
        }

        override fun onViewReleased(releasedChild: View, xVelocity: Float, yVelocity: Float) {
            super.onViewReleased(releasedChild, xVelocity, yVelocity)
            resetDragOnRelease()

            // Check velocity first and then distance
            val returnToOriginal =
                !velocityRelease(xVelocity, yVelocity) && !distanceRelease()

            // settle the view to it's original  position
            if (returnToOriginal) {
                settleViewAt(paddingLeft, paddingTop)
            } else mDismiss = true
            resetOffsets()
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            if (state == ViewDragHelper.STATE_IDLE) {
                if (mDismiss) { // The view is out of screen
                    dismiss()
                }
            }
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return mWidth
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return mHeight
        }

        /**
         * Should Clamp if any scrollable views can scroll
         */
        private fun shouldClampHorizontalScrollViews(left: Int): Boolean {
            var canScrollViewScroll = false

            for (innerScrollView in mInnerScrollViewsList) {
                if (innerScrollView.contains(mPointerX, mPointerY)) {
                    if (selectedLeftDragBack() && left > 0) {
                        canScrollViewScroll = canViewScrollRight(innerScrollView)
                    }
                    if (selectedRightDragBack() && left < 0) {
                        canScrollViewScroll = canViewScrollLeft(innerScrollView)
                    }
                }
            }

            return canScrollViewScroll
        }

        /**
         * Should Clamp if any scrollable views can scroll
         */
        private fun shouldClampVerticalScrollViews(top: Int): Boolean {
            var canScrollViewScroll = false

            for (innerScrollView in mInnerScrollViewsList) {
                if (innerScrollView.contains(mPointerX, mPointerY)) {
                    if (selectedTopDragBack() && top > 0) {
                        canScrollViewScroll = canViewScrollDown(innerScrollView)
                    }
                    if (selectedBottomDragBack() && top < 0) {
                        canScrollViewScroll = canViewScrollUp(innerScrollView)
                    }
                }
            }

            return canScrollViewScroll
        }

        private fun onDragFromLeft(left: Int) {
            mIsDraggedFromLeft = true
            mLeftOffset = left.coerceAtLeast(paddingLeft).coerceAtMost(mWidth)
        }

        private fun onDragFromTop(top: Int) {
            mIsDraggedFromTop = true
            mTopOffset = top.coerceAtLeast(paddingTop).coerceAtMost(mHeight)
        }

        private fun onDragFromRight(left: Int) {
            mIsDraggedFromRight = true
            mLeftOffset = left.coerceAtLeast(-mWidth).coerceAtMost(paddingRight)
        }

        private fun onDragFromBottom(top: Int) {
            mIsDraggedFromBottom = true
            mTopOffset = top.coerceAtLeast(-mHeight).coerceAtMost(paddingBottom)
        }

        private fun canViewScrollLeft(innerScrollView: View): Boolean {
            return innerScrollView.canViewScrollLeft(mPointerX, mPointerY)
        }

        private fun canViewScrollRight(innerScrollView: View): Boolean {
            return innerScrollView.canViewScrollRight(mPointerX, mPointerY)
        }

        private fun canViewScrollDown(innerScrollView: View): Boolean {
            return innerScrollView.canViewScrollDown(mPointerX, mPointerY)
        }

        private fun canViewScrollUp(innerScrollView: View): Boolean {
            return innerScrollView.canViewScrollUp(mPointerX, mPointerY)
        }

        /**
         *  If the directed velocity greater than the set [mDragDismissVelocity] then settle the view out of screen
         */
        private fun velocityRelease(xVelocity: Float, yVelocity: Float): Boolean {
            if (mDragDismissVelocity <= 0) // LEVEL_0 turns off the velocity dismiss
                return false

            if (xVelocity > mDragDismissVelocity && selectedLeftDragBack()) {
                settleViewAt(mWidth, mTopOffset)
                return true
            } else if (xVelocity < -mDragDismissVelocity && selectedRightDragBack()) {
                settleViewAt(-mWidth, mTopOffset)
                return true
            } else if (yVelocity > mDragDismissVelocity && selectedTopDragBack()) {
                settleViewAt(mLeftOffset, mHeight)
                return true
            } else if (yVelocity < -mDragDismissVelocity && selectedBottomDragBack()) {
                settleViewAt(mLeftOffset, -mHeight)
                return true
            }
            return false
        }

        /**
         *  If the distance traveled greater than the set [mDragDismissScreenPercentage] then settle the view out of screen
         */
        private fun distanceRelease(): Boolean {
            if (mSwipeBackFraction >= mDragDismissScreenPercentage) {
                if (abs(mLeftOffset) > abs(mTopOffset)) {
                    if (mLeftOffset > 0) {
                        settleViewAt(mWidth, mTopOffset)
                    } else {
                        settleViewAt(-mWidth, mTopOffset)
                    }
                } else if (mTopOffset > 0) {
                    settleViewAt(mLeftOffset, mHeight)
                } else {
                    settleViewAt(mLeftOffset, -mHeight)
                }
                return true
            }
            return false
        }

        private fun resetDragOnRelease() {
            mIsDraggedFromRight = false
            mIsDraggedFromLeft = false
            mIsDraggedFromBottom = false
            mIsDraggedFromTop = false
        }
    }

    private fun resetOffsets() {
        mLeftOffset = 0
        mTopOffset = 0
    }

    private fun selectedBottomDragBack() =
        false

    private fun selectedTopDragBack() =
        mSelectedDragBackDirections == DragDismissDirections.FROM_TOP

    private fun selectedRightDragBack() =
        mSelectedDragBackDirections == DragDismissDirections.FROM_RIGHT

    private fun selectedLeftDragBack() =
        mSelectedDragBackDirections == DragDismissDirections.FROM_LEFT

    /**
     * Moves the view's left and top.
     */
    private fun settleViewAt(left: Int, top: Int) {
        if (mDragHelper.smoothSlideViewTo(mDraggedView, left, top))
            ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun dismiss() {
        mDismissCallback!!.invoke()
    }

    /**
     * Returns the starting alpha of the canvas background for the dismiss background fade out effect.
     *
     * @return The currently set percentage.
     */
    @IntRange(from = 0, to = 100)
    fun getBackgroundDim(): Int {
        return (Utilities.calculateFractionFromAlpha(mBackgroundDim) * 100).toInt()
    }

    /**
     * Sets The starting alpha of the canvas background for the dismiss background fade out effect.
     *
     * @param backgroundDim The percentage to set from 0 to 100.
     * @default 80
     */
    fun setBackgroundDim(@IntRange(from = 0, to = 100) backgroundDim: Int) {
        val fraction = Utilities.fractionFromPercentage(backgroundDim)
        mBackgroundDim = Utilities.calculateAlphaFromFraction(fraction)
    }

    /**
     * Sets The drag directions (Can be more than one direction) from [DragDismissDirections]
     *
     * @param directions The directions to set from [DragDismissDirections]
     * @default [DragDismissDirections.FROM_LEFT]
     */
    fun setDraggingDirections(directions: DragDismissDirections) {
        mSelectedDragBackDirections = directions
    }

    /**
     * Returns the percentage of the screen traveled before the screen is dismissed on release.
     *
     * @return The currently set percentage.
     */
    fun getDragDismissScreenPercentage(): Int {
        return Utilities.percentageFromFraction(mDragDismissScreenPercentage)
    }

    /**
     * Sets the percentage of the screen traveled before the screen is dismissed on release.
     *
     * @param screenPercentage The percentage to set from 0 to 100.
     * @default 40
     */
    fun setDragDismissScreenPercentage(@IntRange(from = 0, to = 100) screenPercentage: Int) {
        mDragDismissScreenPercentage = Utilities.fractionFromPercentage(screenPercentage)
    }

    /**
     * Sets the speed level that if the screen is flung past, It will be dismissed on release.
     *
     * @param dragDismissVelocityLevel The level to set.
     * @default [DragDismissVelocityLevel.LEVEL_3]
     * @see mDragDismissScreenPercentage
     */
    fun setDragDismissVelocityLevel(dragDismissVelocityLevel: DragDismissVelocityLevel) {
        mDragDismissVelocity = dragDismissVelocityLevel.velocity
    }

    /**
     * Sets the callback to be invoked when the Dragging is done.
     *
     * @param dismissCallback the callback to set.
     */
    fun setDismissCallback(dismissCallback: (() -> Unit)?) {
        mDismissCallback = dismissCallback
    }
}
