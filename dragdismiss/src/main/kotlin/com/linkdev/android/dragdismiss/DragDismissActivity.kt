package com.linkdev.android.dragdismiss

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

// Created by Mohammed Fareed on 6/1/2020.
// Copyright (c) 2020 Link Development All rights reserved.
class DragDismissActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, @LayoutRes activityLayout: Int) {
            val starter = Intent(context, DragDismissActivity::class.java)
            starter.putExtra("layout", activityLayout)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = intent.getIntExtra("layout", -1)

        check(layout != -1) { "No layout set for the DragDismissActivity" }

        val view = layoutInflater.inflate(layout, DragDismissLayout(this))
        setContentView(view)
    }
}
