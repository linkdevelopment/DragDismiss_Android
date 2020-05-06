package com.linkdev.dragDismiss

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.dragDismiss.sample_activities.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this

        btnDefault.setOnClickListener { ActivityDefault.startActivity(mContext!!) }
        btnEdgeTrackingLeft.setOnClickListener { ActivityEdgeTrackingLeft.startActivity(mContext!!) }
        btnRecyclerView.setOnClickListener { ActivityRecyclerView.startActivity(mContext!!) }
        btnNestedScrollView.setOnClickListener { ActivityNestedScrollView.startActivity(mContext!!) }
        btnHorizontalScrollView.setOnClickListener { ActivityHorizontalRecyclerView.startActivity(mContext!!) }
    }
}