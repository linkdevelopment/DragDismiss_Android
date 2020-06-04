package com.linkdev.dragDismiss.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.android.dragdismiss.DragDismiss
import com.linkdev.dragDismiss.R
import com.linkdev.dragDismiss.utils.Extras
import com.linkdev.dragDismiss.utils.SampleDismissAttrs
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
        val sampleAttrs = intent.getParcelableExtra<SampleDismissAttrs>(Extras.EXTRA_SAMPLE_ATTRS)!!
        DragDismiss.create(mContext)
            .setDragDismissScreenPercentage(sampleAttrs.dragDismissScreenPercentage)
            .setDragDismissVelocityLevel(sampleAttrs.dragDragDismissVelocityLevel)
            .setShouldDragEdgeOnly(sampleAttrs.shouldDragEdgeOnly)
            .setDragDismissDraggingDirections(sampleAttrs.draggingDirections)
            .setDragDismissBackgroundAlpha(sampleAttrs.backgroundAlpha)
            .attach(this, R.layout.activity_nested_scroll_view)

        setSupportActionBar(toolbar)
    }
}
