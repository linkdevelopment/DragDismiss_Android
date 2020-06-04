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
import com.linkdev.android.dragdismiss.DragDismiss
import com.linkdev.dragDismiss.R
import com.linkdev.dragDismiss.utils.Extras
import com.linkdev.dragDismiss.utils.SampleDismissAttrs
import kotlinx.android.synthetic.main.activity_recycler_view.recyclerView
import kotlinx.android.synthetic.main.activity_recycler_view.toolbar
import java.util.*

class ActivityHorizontalRecyclerView : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, sampleAttrs: SampleDismissAttrs) {
            val starter = Intent(context, ActivityHorizontalRecyclerView::class.java)
            starter.putExtra(Extras.EXTRA_SAMPLE_ATTRS, sampleAttrs)
            context.startActivity(starter)
        }
    }

    private var mContext: Context = this

    private val mDataList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sampleAttrs = intent.getParcelableExtra<SampleDismissAttrs>(Extras.EXTRA_SAMPLE_ATTRS)!!
        DragDismiss.create(mContext)
            .setDragDismissScreenPercentage(sampleAttrs.dragDismissScreenPercentage)
            .setDragDismissVelocityLevel(sampleAttrs.dragDragDismissVelocityLevel)
            .setShouldDragEdgeOnly(sampleAttrs.shouldDragEdgeOnly)
            .setDragDismissDraggingDirections(sampleAttrs.draggingDirections)
            .setDragDismissBackgroundAlpha(sampleAttrs.backgroundAlpha)
            .attach(this, R.layout.activity_horizontal_recycler_view)

        setSupportActionBar(toolbar)

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        initDataList()
        recyclerView.adapter = adapter
    }

    private fun initDataList() {
        for (i in 0..10) {
            mDataList.add(i)
        }
    }

    private val adapter: RecyclerView.Adapter<ViewHolder> =
        object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val textView = TextView(this@ActivityHorizontalRecyclerView)
                textView.layoutParams = RecyclerView.LayoutParams(500, 500)
                textView.gravity = Gravity.CENTER
                return ViewHolder(textView)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.tv.text = "${mDataList[position]}"
                holder.tv.setTextColor(
                    ContextCompat.getColor(
                        this@ActivityHorizontalRecyclerView,
                        R.color.colorAccent
                    )
                )
                if (mDataList[position] % 2 == 0) {
                    holder.tv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ActivityHorizontalRecyclerView, R.color.colorPrimary
                        )
                    )
                } else {
                    holder.tv.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ActivityHorizontalRecyclerView, R.color.colorPrimaryDark
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