package com.linkdev.dragDismiss

import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.linkdev.android.dragdismiss.layout.DragDismissDirections
import com.linkdev.android.dragdismiss.layout.DragDismissVelocityLevel
import com.linkdev.dragDismiss.sample_activities.ActivityHorizontalRecyclerView
import com.linkdev.dragDismiss.sample_activities.ActivityNestedScrollView
import com.linkdev.dragDismiss.sample_activities.ActivityRecyclerView
import com.linkdev.dragDismiss.utils.SampleDismissAttrs
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.section_drag_dismiss_values.*
import kotlinx.android.synthetic.main.section_dragging_directions.*

// Created by Mohammed Fareed on 9/28/2020.
// Copyright (c) 2020 Link Development All rights reserved.
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var mListener: IMainFragmentInteraction
    private lateinit var mContext: Context

    companion object {
        const val SEEKBAR_DISTANCE_MIN = 30
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
        setSampleActivitiesClickListeners()

        setupDragDismissAttrs()
    }

    private fun setSampleActivitiesClickListeners() {
        btnRecyclerView.setOnClickListener {
            ActivityRecyclerView.startActivity(
                mContext,
                getDragDismissAttrs()
            )
        }
        btnNestedScrollView.setOnClickListener {
            ActivityNestedScrollView.startActivity(
                mContext,
                getDragDismissAttrs()
            )
        }
        btnHorizontalScrollView.setOnClickListener {
            ActivityHorizontalRecyclerView.startActivity(
                mContext, getDragDismissAttrs()
            )
        }
        btnFragment.setOnClickListener {
            mListener.onFragmentClicked(getDragDismissAttrs())
        }
    }

    private fun getDragDismissAttrs(): SampleDismissAttrs {
        val selectedDirections = getSelectedDirections()
        val velocityLevel =
            DragDismissVelocityLevel.values()[seekbarVelocity.progress]
        val dragDismissFactor = (seekbarDistance.progress.toFloat() + SEEKBAR_DISTANCE_MIN) / 100
        val backgroundAlpha = seekbarBackgroundAlpha.progress.toFloat() / 100

        return SampleDismissAttrs(
            dragDismissFactor,
            velocityLevel,
            checkboxEdgeDrag.isChecked,
            selectedDirections,
            backgroundAlpha
        )
    }

    private fun getSelectedDirections(): Int {
        if (checkboxAll.isChecked) {
            return DragDismissDirections.DIRECTION_ALL
        }
        val selectedDirections = arrayListOf<Int>()

        if (checkboxBottom.isChecked)
            selectedDirections.add(DragDismissDirections.DIRECTION_FROM_BOTTOM)
        if (checkboxTop.isChecked)
            selectedDirections.add(DragDismissDirections.DIRECTION_FROM_TOP)
        if (checkboxLeft.isChecked)
            selectedDirections.add(DragDismissDirections.DIRECTION_FROM_LEFT)
        if (checkboxRight.isChecked)
            selectedDirections.add(DragDismissDirections.DIRECTION_FROM_RIGHT)

        if (selectedDirections.isEmpty())
            return 0

        var directions = 0
        for (direction in selectedDirections)
            directions = directions or direction

        return directions
    }

    private fun setupDragDismissAttrs() {
        seekbarVelocity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvVelocityProgress.text = seekbarVelocity.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekbarDistance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvDistanceProgress.text =
                    (seekbarDistance.progress + SEEKBAR_DISTANCE_MIN).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekbarBackgroundAlpha.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvAlphaProgress.text =
                    (seekbarBackgroundAlpha.progress).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    interface IMainFragmentInteraction {
        fun onFragmentClicked(dragDismissAttrs: SampleDismissAttrs)
    }
}
