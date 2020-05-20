package com.linkdev.dragDismiss

import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.android.dragdismiss.DragDismissLayout
import com.linkdev.dragDismiss.sample_activities.ActivityHorizontalRecyclerView
import com.linkdev.dragDismiss.sample_activities.ActivityNestedScrollView
import com.linkdev.dragDismiss.sample_activities.ActivityRecyclerView
import com.linkdev.dragDismiss.utils.SampleDismissAttrs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.section_drag_dismiss_values.*
import kotlinx.android.synthetic.main.section_dragging_directions.*

class MainActivity : AppCompatActivity() {

    private var mContext: Context? = null

    val SEEKBAR_DISTANCE_MIN = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        setSampleActivitiesClickListeners()

        setupDragDismissAttrs()
    }

    private fun setSampleActivitiesClickListeners() {
        btnRecyclerView.setOnClickListener {
            ActivityRecyclerView.startActivity(
                mContext!!,
                getDragDismissAttrs()
            )
        }
        btnNestedScrollView.setOnClickListener {
            ActivityNestedScrollView.startActivity(
                mContext!!,
                getDragDismissAttrs()
            )
        }
        btnHorizontalScrollView.setOnClickListener {
            ActivityHorizontalRecyclerView.startActivity(
                mContext!!, getDragDismissAttrs()
            )
        }
    }

    private fun getDragDismissAttrs(): SampleDismissAttrs {
        val selectedDirections = getSelectedDirections()
        val velocityLevel =
            DragDismissLayout.DismissVelocityLevel.values()[seekbarVelocity.progress]
        val dragDismissFactor = (seekbarDistance.progress.toFloat() + SEEKBAR_DISTANCE_MIN) / 100

        return SampleDismissAttrs(
            dragDismissFactor,
            velocityLevel,
            checkboxEdgeDrag.isChecked,
            selectedDirections
        )
    }

    private fun getSelectedDirections(): Int {
        if (checkboxAll.isChecked) {
            return DragDismissLayout.DragDirections.DIRECTION_ALL
        }
        val selectedDirections = arrayListOf<Int>()

        if (checkboxBottom.isChecked)
            selectedDirections.add(DragDismissLayout.DragDirections.DIRECTION_FROM_BOTTOM)
        if (checkboxTop.isChecked)
            selectedDirections.add(DragDismissLayout.DragDirections.DIRECTION_FROM_TOP)
        if (checkboxLeft.isChecked)
            selectedDirections.add(DragDismissLayout.DragDirections.DIRECTION_FROM_LEFT)
        if (checkboxRight.isChecked)
            selectedDirections.add(DragDismissLayout.DragDirections.DIRECTION_FROM_RIGHT)

        if (selectedDirections.isEmpty())
            return 0

        var directions: Int = 0
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
    }
}
