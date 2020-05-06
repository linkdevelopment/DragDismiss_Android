package com.linkdev.dragDismiss.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.dragDismiss.R
import kotlinx.android.synthetic.main.activity_nested_scroll_view.*

class ActivityNestedScrollView : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            val starter = Intent(context, ActivityNestedScrollView::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_view)
        toolbar?.title = "Swipe from left or bottom"
        setSupportActionBar(toolbar)
    }
}
