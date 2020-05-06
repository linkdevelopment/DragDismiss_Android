package com.linkdev.dragDismiss.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.dragDismiss.R

class ActivityDefault : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)
    }

    companion object {
        fun startActivity(context: Context) {
            val starter = Intent(context, ActivityDefault::class.java)
            context.startActivity(starter)
        }
    }
}