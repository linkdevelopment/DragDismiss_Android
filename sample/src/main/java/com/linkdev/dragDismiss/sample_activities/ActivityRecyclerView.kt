package com.linkdev.dragDismiss.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linkdev.dragDismiss.R
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.util.*

class ActivityRecyclerView : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            val starter = Intent(context, ActivityRecyclerView::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var mDataList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        setSupportActionBar(toolbar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mDataList = ArrayList()
        for (i in 0..49) {
            mDataList.add("$i")
        }
        recyclerView.adapter = adapter
    }

    private val adapter: RecyclerView.Adapter<ViewHolder> =
        object :
            RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val textView = TextView(this@ActivityRecyclerView)
                textView.layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    100
                )
                textView.gravity = Gravity.CENTER
                return ViewHolder(textView)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.tv.text = mDataList[position]
            }

            override fun getItemCount(): Int {
                return mDataList.size
            }
        }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView = itemView as TextView
    }
}