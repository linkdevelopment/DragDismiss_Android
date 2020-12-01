package com.linkdev.dragdismisssample

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.linkdev.dragdismiss.models.DragDismissDefaults
import com.linkdev.dragdismiss.models.DragDismissDirections
import com.linkdev.dragdismiss.models.DragDismissVelocityLevel
import com.linkdev.dragdismisssample.sample_fragments.FragmentHorizontalRecyclerView
import com.linkdev.dragdismisssample.sample_fragments.FragmentNestedScrollView
import com.linkdev.dragdismisssample.sample_fragments.FragmentRecyclerView
import com.linkdev.dragdismisssample.sample_fragments.FragmentWebView
import com.linkdev.dragdismisssample.sample_fragments.viewpager_sample.FragmentViewPager
import com.linkdev.dragdismisssample.sample_activities.ActivitySample
import com.linkdev.dragdismisssample.utils.SampleDismissAttrs
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
    }

    private fun setSampleActivitiesClickListeners() {
        btnRecyclerView.setOnClickListener {
            mListener.onFragmentClicked(
                FragmentRecyclerView.newInstance(getDragDismissAttrs()),
                FragmentRecyclerView.TAG
            )
        }
        btnViewPager.setOnClickListener {
            mListener.onFragmentClicked(
                FragmentViewPager.newInstance(getDragDismissAttrs()),
                FragmentViewPager.TAG
            )
        }
        btnNestedScrollView.setOnClickListener {
            mListener.onFragmentClicked(
                FragmentNestedScrollView.newInstance(getDragDismissAttrs()),
                FragmentNestedScrollView.TAG
            )
        }
        btnHorizontalScrollView.setOnClickListener {
            mListener.onFragmentClicked(
                FragmentHorizontalRecyclerView.newInstance(getDragDismissAttrs()),
                FragmentHorizontalRecyclerView.TAG
            )
        }
        btnWebView.setOnClickListener {
            mListener.onFragmentClicked(
                FragmentWebView.newInstance(getDragDismissAttrs()),
                FragmentWebView.TAG
            )
        }
        btnActivity.setOnClickListener {
            ActivitySample.startActivity(mContext, getDragDismissAttrs())
        }
    }

    private fun getDragDismissAttrs(): SampleDismissAttrs {
        val selectedDirections = getSelectedDirections()
        val velocityLevel = DragDismissVelocityLevel.values()[spVelocity.selectedItemPosition]
        val dragDismissScreenPercentage = seekbarDistance.progress
        val backgroundDim = seekbarBackgroundDim.progress

        return SampleDismissAttrs(
            dragDismissScreenPercentage,
            velocityLevel,
            selectedDirections,
            backgroundDim
        )
    }

    private fun getSelectedDirections(): DragDismissDirections {
        return when {
            checkboxTop.isChecked ->
                DragDismissDirections.FROM_TOP
            checkboxLeft.isChecked ->
                DragDismissDirections.FROM_LEFT
            checkboxRight.isChecked ->
                DragDismissDirections.FROM_RIGHT
            else -> DragDismissDirections.FROM_LEFT
        }
    }

    private fun setupDragDismissAttrs() {
        seekbarDistance.setOnSeekBarChangeListener(onSeekBarDistanceChangeListener())

        seekbarBackgroundDim.setOnSeekBarChangeListener(onSeekBarBackgroundChangeListener())
    }

    private fun onSeekBarBackgroundChangeListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvDimProgress.text =
                    "${seekbarBackgroundDim.progress}%"
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
        fun onFragmentClicked(fragment: Fragment, tag: String)
    }
}
