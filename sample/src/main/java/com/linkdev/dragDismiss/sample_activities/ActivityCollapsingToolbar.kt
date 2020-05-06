package com.linkdev.dragDismiss.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.dragDismiss.R
import kotlinx.android.synthetic.main.activity_collapsing_toolbar.*

class ActivityCollapsingToolbar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapsing_toolbar)

        setSupportActionBar(toolbar)

        collapsingToolbar.title = "Swipe from left edge"
    }

    companion object {
        fun startActivity(context: Context) {
            val starter = Intent(context, ActivityCollapsingToolbar::class.java)
            context.startActivity(starter)
        }
    }
}
