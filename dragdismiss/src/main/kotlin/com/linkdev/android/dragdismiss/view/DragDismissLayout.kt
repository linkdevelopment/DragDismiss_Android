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

package com.linkdev.android.dragdismiss.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.linkdev.android.dragdismiss.R
import com.linkdev.android.dragdismiss.models.DragDismissDefaults
import com.linkdev.android.dragdismiss.models.DragDismissDirections
import com.linkdev.android.dragdismiss.models.DragDismissVelocityLevel
import com.linkdev.android.dragdismiss.utils.*
import java.util.*
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
     * The starting alpha of the canvas background for the dismiss background fade out effect.
     *
     * @default [DragDismissDefaults.DEFAULT_BACKGROUND_ALPHA_FRACTION]
     */
    private var mBackgroundAlpha =
        Utilities.calculateAlphaFromFraction(DragDismissDefaults.DEFAULT_BACKGROUND_ALPHA_FRACTION)

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
     * If should dismiss if dragged from the edges of selected directions only.
     */
    private var mShouldDragEdgeOnly = DragDismissDefaults.DEFAULT_SHOULD_DRAG_EDGE_ONLY

    /**
     * The Currently touched edge.
     */
    private var mTouchedEdge = ViewDragHelper.INVALID_POINTER

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
    private lateinit var mSelectedDragBackDirections: ArrayList<Int>

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
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragDismissLayout)

        val directionFlags =
            typedArray.getInt(
                R.styleable.DragDismissLayout_draggingDirections,
                DragDismissDefaults.DEFAULT_DRAG_DIRECTION
            )
        mSelectedDragBackDirections =
            Utilities.extractDirectionsFromFlag(directionFlags)

        mDragDismissScreenPercentage =
            typedArray.getFraction(
                R.styleable.DragDismissLayout_dragDismissScreenPercentage,
                1,
                1,
                DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE
            )

        mBackgroundAlpha =
            Utilities.calculateAlphaFromFraction(
                typedArray.getFraction(
                    R.styleable.DragDismissLayout_backgroundAlpha,
                    1,
                    1,
                    DragDismissDefaults.DEFAULT_BACKGROUND_ALPHA_FRACTION
                )
            )

        mShouldDragEdgeOnly =
            typedArray.getBoolean(
                R.styleable.DragDismissLayout_shouldDragEdgeOnly,
                DragDismissDefaults.DEFAULT_SHOULD_DRAG_EDGE_ONLY
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
            (mBackgroundAlpha - (mBackgroundAlpha * mSwipeBackFraction)).toInt(),
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
            MotionEvent.ACTION_MOVE ->
                // Handle children scrollable views touch interception.
                if (mInnerScrollViewsList.isNotEmpty()) {
                    val xOffset = abs(event.rawX - mPointerX)
                    val yOffset = abs(event.rawY - mPointerY)
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
                                mDragHelper.shouldInterceptTouchEvent(event) ||
                                        super.onInterceptTouchEvent(event)
                            else super.onInterceptTouchEvent(event)
                        }
                    }
                }
        }
        return mDragHelper.shouldInterceptTouchEvent(event) || super.onInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDragHelper.processTouchEvent(event!!)
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
            if (!edgeSwipe()) return paddingLeft

            // Clamp the view if it's not All Drag and not dragged from left and not dragged from right
            if (!selectedAllDragBack() && !selectedLeftDragBack() && !selectedRightDragBack())
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
            if (!edgeSwipe()) return paddingTop

            // Clamp the view if it's not All Drag and not dragged from top and not dragged from bottom
            if (!selectedAllDragBack() && !selectedBottomDragBack() && !selectedTopDragBack())
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
                if (absLeft > absTop) 1.0f * absLeft / mWidth
                else 1.0f * absTop / mHeight
            invalidate()
        }

        override fun onViewReleased(releasedChild: View, xVelocity: Float, yVelocity: Float) {
            super.onViewReleased(releasedChild, xVelocity, yVelocity)
            resetDragOnRelease()

            if (!edgeSwipe()) {
                mTouchedEdge = ViewDragHelper.INVALID_POINTER
                resetOffsets()
                return
            }

            // Check velocity first and then distance
            val returnToOriginal =
                !velocityRelease(xVelocity, yVelocity) && !distanceRelease()

            // settle the view to it's original  position
            if (returnToOriginal) {
                settleViewAt(paddingLeft, paddingTop)
            }
            resetOffsets()
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            if (state == ViewDragHelper.STATE_IDLE) {
                if (mSwipeBackFraction >= 1) { // The view is out of screen
                    finish()
                }
            }
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return mWidth
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return mHeight
        }

        override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
            super.onEdgeTouched(edgeFlags, pointerId)
            mTouchedEdge = edgeFlags
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
                    } else if (selectedRightDragBack() && left < 0) {
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
                    } else if (selectedBottomDragBack() && top < 0) {
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

            if (xVelocity > mDragDismissVelocity && (selectedAllDragBack() || selectedLeftDragBack())) {
                settleViewAt(mWidth, mTopOffset)
                return true
            } else if (xVelocity < -mDragDismissVelocity && (selectedAllDragBack() || selectedRightDragBack())) {
                settleViewAt(-mWidth, mTopOffset)
                return true
            } else if (yVelocity > mDragDismissVelocity && (selectedAllDragBack() || selectedTopDragBack())) {
                settleViewAt(mLeftOffset, mHeight)
                return true
            } else if (yVelocity < -mDragDismissVelocity && (selectedAllDragBack() || selectedBottomDragBack())) {
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

    private fun edgeSwipe(): Boolean {
        if (mShouldDragEdgeOnly) {
            var isTouchEnabled = false
            if (selectedBottomDragBack())
                isTouchEnabled = mTouchedEdge == ViewDragHelper.EDGE_BOTTOM
            if (selectedTopDragBack())
                isTouchEnabled = isTouchEnabled || mTouchedEdge == ViewDragHelper.EDGE_TOP
            if (selectedLeftDragBack())
                isTouchEnabled = isTouchEnabled || mTouchedEdge == ViewDragHelper.EDGE_LEFT
            if (selectedRightDragBack())
                isTouchEnabled = isTouchEnabled || mTouchedEdge == ViewDragHelper.EDGE_RIGHT
            return isTouchEnabled
        }
        return true
    }

    private fun selectedAllDragBack() =
        mSelectedDragBackDirections.contains(DragDismissDirections.DIRECTION_ALL)

    private fun selectedBottomDragBack() =
        mSelectedDragBackDirections.contains(DragDismissDirections.DIRECTION_FROM_BOTTOM)

    private fun selectedTopDragBack() =
        mSelectedDragBackDirections.contains(DragDismissDirections.DIRECTION_FROM_TOP)

    private fun selectedRightDragBack() =
        mSelectedDragBackDirections.contains(DragDismissDirections.DIRECTION_FROM_RIGHT)

    private fun selectedLeftDragBack() =
        mSelectedDragBackDirections.contains(DragDismissDirections.DIRECTION_FROM_LEFT)

    /**
     * Moves the view's left and top.
     */
    private fun settleViewAt(left: Int, top: Int) {
        if (mDragHelper.smoothSlideViewTo(mDraggedView, left, top))
            ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun finish() {
        if (context is Activity)
            (context as Activity).overridePendingTransition(0, 0) // remove the ending transition
        if (mDismissCallback == null) {
            if (context is Activity)
                (context as Activity).finish()
        } else {
            mDismissCallback!!.invoke()
        }
    }

    @IntRange(from = 0, to = 255)
    fun getBackgroundAlpha(): Int {
        return mBackgroundAlpha
    }

    /**
     * The starting alpha of the canvas background for the dismiss background fade out effect.
     *
     * @default [DragDismissDefaults.DEFAULT_BACKGROUND_ALPHA_FRACTION]
     */
    fun setBackgroundAlpha(@FloatRange(from = 0.0, to = 1.0) backgroundAlpha: Float) {
        mBackgroundAlpha = Utilities.calculateAlphaFromFraction(backgroundAlpha)
    }

    /**
     * The selected drag directions(Can be more than one direction) from [DragDismissDirections]
     *
     *
     * @default [DragDismissDefaults.DEFAULT_DRAG_DIRECTION]
     */
    fun setDraggingDirections(edges: Int) {
        mSelectedDragBackDirections = Utilities.extractDirectionsFromFlag(edges)
    }

    fun getDragDismissDistance(): Float {
        return mDragDismissScreenPercentage
    }

    /**
     * The distance traveled before the view initiate a dismiss on finger up.
     *
     * So if it's set to [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE] then if the layout is dragged more than [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE] of the screen the view will be dismissed on release.
     *
     * @default [DragDismissDefaults.DEFAULT_DISMISS_SCREEN_PERCENTAGE]
     * @see mDragDismissVelocity
     */
    fun setDragDismissDistance(@FloatRange(from = 0.0, to = 1.0) screenDragDismiss: Float) {
        mDragDismissScreenPercentage = screenDragDismiss
    }

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
    fun setDragDismissVelocityLevel(dragDismissVelocityLevel: DragDismissVelocityLevel) {
        mDragDismissVelocity = dragDismissVelocityLevel.velocity
    }

    fun shouldDragEdgeOnly(): Boolean {
        return mShouldDragEdgeOnly
    }

    /**
     * If should dismiss if dragged from the edges of selected directions only.
     */
    fun setShouldDragEdgeOnly(shouldDragEdgeOnly: Boolean) {
        mShouldDragEdgeOnly = shouldDragEdgeOnly
    }

    fun setDismissCallback(dismissCallback: (() -> Unit)?) {
        mDismissCallback = dismissCallback
    }
}
