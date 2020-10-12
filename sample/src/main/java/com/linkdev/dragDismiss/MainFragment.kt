package com.linkdev.dragDismiss

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.linkdev.android.dragdismiss.models.DragDismissDefaults
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

        initVelocitySpinner()
        setListeners()
    }

    private fun initVelocitySpinner() {
        val velocityLevels =
            arrayListOf("Level 0", "Level 1", "Level 2", "Level 3", "Level 4", "Level 5")

        val arrayAdapter =
            ArrayAdapter(mContext, android.R.layout.simple_spinner_item, velocityLevels)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spVelocity.adapter = arrayAdapter
        spVelocity.setSelection(DragDismissDefaults.DEFAULT_DISMISS_VELOCITY_LEVEL.ordinal)
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
        val velocityLevel = DragDismissVelocityLevel.values()[spVelocity.selectedItemPosition]
        val dragDismissScreenPercentage = seekbarDistance.progress
        val backgroundDim = seekbarBackgroundAlpha.progress

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
                    "${seekbarBackgroundAlpha.progress}%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }

    private fun onSeekBarDistanceChangeListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvDistanceProgress.text = "${seekbarDistance.progress}%"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }

    interface IMainFragmentInteraction {
        fun onFragmentClicked(dragDismissAttrs: SampleDismissAttrs)
    }
}
