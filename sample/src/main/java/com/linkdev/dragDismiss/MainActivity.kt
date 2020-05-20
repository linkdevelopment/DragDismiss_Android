package com.linkdev.dragDismiss

import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.android.dragdismiss.DragDismissLayout
import com.linkdev.dragDismiss.sample_activities.*
import com.linkdev.dragDismiss.utils.SampleDismissAttrs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.section_drag_dismiss_values.*
import kotlinx.android.synthetic.main.section_dragging_directions.*

class MainActivity : AppCompatActivity() {

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        setSampleActivitiesClickListeners()

        setupDragDismissAttrs()
    }

    private fun setSampleActivitiesClickListeners() {
        btnDefault.setOnClickListener { ActivityDefault.startActivity(mContext!!) }
        btnCollapsingToolbar.setOnClickListener {
            ActivityCollapsingToolbar.startActivity(
                mContext!!,
                getDragDismissAttrs()
            )
        }
        btnRecyclerView.setOnClickListener { ActivityRecyclerView.startActivity(mContext!!) }
        btnNestedScrollView.setOnClickListener { ActivityNestedScrollView.startActivity(mContext!!) }
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

        return SampleDismissAttrs(
            seekbarDistance.progress.toFloat(),
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
                tvDistanceProgress.text = (seekbarDistance.progress + 30).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
