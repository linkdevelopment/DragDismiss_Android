package com.linkdev.dragDismiss.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.linkdev.dragDismiss.R
import kotlinx.android.synthetic.main.activity_edge_tracking_left.*

class ActivityEdgeTrackingLeft : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edge_tracking_left)

        setSupportActionBar(toolbar)

        collapsing_toolbar.title = "Swipe from left edge"
    }

    companion object {
        fun startActivity(context: Context) {
            val starter = Intent(context, ActivityEdgeTrackingLeft::class.java)
            context.startActivity(starter)
        }
    }
}