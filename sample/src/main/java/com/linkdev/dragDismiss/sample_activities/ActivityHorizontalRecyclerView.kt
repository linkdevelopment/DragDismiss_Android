package com.linkdev.dragDismiss.sample_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linkdev.dragDismiss.R
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.util.*

class ActivityHorizontalRecyclerView : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            val starter = Intent(context, ActivityHorizontalRecyclerView::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var mDataList: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_scroll_view)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mDataList = ArrayList()
        for (i in 0..19) {
            mDataList.add(i)
        }
        recyclerView.adapter = adapter
    }

    private val adapter: RecyclerView.Adapter<ViewHolder> =
        object :
            RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val textView = TextView(this@ActivityHorizontalRecyclerView)
                textView.layoutParams = RecyclerView.LayoutParams(500, 500)
                textView.gravity = Gravity.CENTER
                return ViewHolder(textView)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.tv.text = "${mDataList[position]}"
                if (mDataList[position] % 2 == 0) {
                    holder.tv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ActivityHorizontalRecyclerView, android.R.color.darker_gray
                        )
                    )
                    holder.tv.setTextColor(
                        ContextCompat.getColor(
                            this@ActivityHorizontalRecyclerView,
                            android.R.color.white
                        )
                    )
                } else {
                    holder.tv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ActivityHorizontalRecyclerView,
                            android.R.color.white
                        )
                    )
                    holder.tv.setTextColor(
                        ContextCompat.getColor(
                            this@ActivityHorizontalRecyclerView, android.R.color.darker_gray
                        )
                    )
                }
            }

            override fun getItemCount(): Int {
                return mDataList.size
            }
        }

    internal class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var tv: TextView = itemView as TextView
    }
}