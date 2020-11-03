package com.linkdev.dragdismisssample.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.dragdismiss.DragDismiss
import com.linkdev.dragdismisssample.R
import com.linkdev.dragdismisssample.utils.Extras
import com.linkdev.dragdismisssample.utils.SampleDismissAttrs
import kotlinx.android.synthetic.main.activity_nested_scroll_view.*

class ActivityNestedScrollView : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, sampleDismissAttrs: SampleDismissAttrs) {
            val starter = Intent(context, ActivityNestedScrollView::class.java)
            starter.putExtra(Extras.EXTRA_SAMPLE_ATTRS, sampleDismissAttrs)
            context.startActivity(starter)
        }
    }

    private var mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getDragDismissContentView())

        setSupportActionBar(toolbar)
    }

    private fun getDragDismissContentView(): View {
        val sampleAttrs = intent.getParcelableExtra<SampleDismissAttrs>(Extras.EXTRA_SAMPLE_ATTRS)!!
        return DragDismiss.create(mContext)
            .setDragScreenPercentage(sampleAttrs.dragDismissScreenPercentage)
            .setDragVelocityLevel(sampleAttrs.dragDragDismissVelocityLevel)
            .setDragDismissDirections(*sampleAttrs.draggingDirections.toTypedArray())
            .setDragBackgroundDimPercentage(sampleAttrs.backgroundDim)
            .attach(this, R.layout.activity_nested_scroll_view)
    }
}
