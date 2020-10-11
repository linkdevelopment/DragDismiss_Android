package com.linkdev.dragDismiss

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.linkdev.android.dragdismiss.models.DragDismissDirections
import com.linkdev.android.dragdismiss.models.DragDismissVelocityLevel
import com.linkdev.dragDismiss.sample_activities.ActivityHorizontalRecyclerView
import com.linkdev.dragDismiss.sample_activities.ActivityNestedScrollView
import com.linkdev.dragDismiss.sample_activities.ActivityRecyclerView
import com.linkdev.dragDismiss.sample_activities.ActivityWebView
import com.linkdev.dragDismiss.sample_activities.viewpager_sample.ActivityViewPager
import com.linkdev.dragDismiss.utils.SampleDismissAttrs
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.section_drag_dismiss_values.*
import kotlinx.android.synthetic.main.section_dragging_directions.*

// Copyright (c) 2020 Link Development All rights reserved.
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var mListener: IMainFragmentInteraction
    private lateinit var mContext: Context

    companion object {
        const val SEEKBAR_SCREEN_PERCENTAGE_MIN = 30
        const val TAG = "MainFragment"

        fun newInstance() = MainFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as IMainFragmentInteraction
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = requireActivity()

        setListeners()
    }

    private fun setListeners() {
        setSampleActivitiesClickListeners()

        setupDragDismissAttrs()

        setCheckListeners()
    }

    private fun setCheckListeners() {
        checkboxAll.setOnCheckedChangeListener { com, isChecked -> onAllChecked(com, isChecked) }
        checkboxTop.setOnCheckedChangeListener { _, _ -> onDirectionChecked() }
        checkboxBottom.setOnCheckedChangeListener { _, _ -> onDirectionChecked() }
        checkboxLeft.setOnCheckedChangeListener { _, _ -> onDirectionChecked() }
        checkboxRight.setOnCheckedChangeListener { _, _ -> onDirectionChecked() }
    }

    private fun setSampleActivitiesClickListeners() {
        btnRecyclerView.setOnClickListener {
            ActivityRecyclerView.startActivity(mContext, getDragDismissAttrs())
        }
        btnViewPager.setOnClickListener {
            ActivityViewPager.startActivity(mContext, getDragDismissAttrs())
        }
        btnNestedScrollView.setOnClickListener {
            ActivityNestedScrollView.startActivity(mContext, getDragDismissAttrs())
        }
        btnHorizontalScrollView.setOnClickListener {
            ActivityHorizontalRecyclerView.startActivity(mContext, getDragDismissAttrs())
        }
        btnWebView.setOnClickListener {
            ActivityWebView.startActivity(mContext, getDragDismissAttrs())
        }
        btnFragment.setOnClickListener {
            mListener.onFragmentClicked(getDragDismissAttrs())
        }
    }

    private fun getDragDismissAttrs(): SampleDismissAttrs {
        val selectedDirections = getSelectedDirections()
        val velocityLevel =
            DragDismissVelocityLevel.values()[seekbarVelocity.progress]
        val dragDismissScreenPercentage =
            (seekbarDistance.progress.toFloat() + SEEKBAR_SCREEN_PERCENTAGE_MIN) / 100
        val backgroundDim = seekbarBackgroundAlpha.progress.toFloat() / 100

        return SampleDismissAttrs(
            dragDismissScreenPercentage,
            velocityLevel,
            selectedDirections,
            backgroundDim
        )
    }

    private fun getSelectedDirections(): Int {
        if (checkboxAll.isChecked)
            return DragDismissDirections.ALL

        var directions = 0
        if (checkboxBottom.isChecked)
            directions = directions or DragDismissDirections.FROM_BOTTOM
        if (checkboxTop.isChecked)
            directions = directions or DragDismissDirections.FROM_TOP
        if (checkboxLeft.isChecked)
            directions = directions or DragDismissDirections.FROM_LEFT
        if (checkboxRight.isChecked)
            directions = directions or DragDismissDirections.FROM_RIGHT

        return directions
    }

    private fun setupDragDismissAttrs() {
        seekbarVelocity.setOnSeekBarChangeListener(onSeekBarVelocityChangeListener())

        seekbarDistance.setOnSeekBarChangeListener(onSeekBarDistanceChangeListener())

        seekbarBackgroundAlpha.setOnSeekBarChangeListener(onSeekBarBackgroundChangeListener())
    }

    private fun onDirectionChecked() {
        checkboxAll.isChecked =
            checkboxTop.isChecked && checkboxBottom.isChecked && checkboxLeft.isChecked && checkboxRight.isChecked
    }

    private fun onAllChecked(compoundButton: CompoundButton, checked: Boolean) {
        if (!compoundButton.isPressed)
            return

        checkboxTop.isChecked = checked
        checkboxBottom.isChecked = checked
        checkboxLeft.isChecked = checked
        checkboxRight.isChecked = checked
    }

    private fun onSeekBarBackgroundChangeListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvAlphaProgress.text =
                    (seekbarBackgroundAlpha.progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }

    private fun onSeekBarDistanceChangeListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvDistanceProgress.text =
                    (seekbarDistance.progress + SEEKBAR_SCREEN_PERCENTAGE_MIN).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }

    private fun onSeekBarVelocityChangeListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvVelocityProgress.text = seekbarVelocity.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }

    interface IMainFragmentInteraction {
        fun onFragmentClicked(dragDismissAttrs: SampleDismissAttrs)
    }
}
