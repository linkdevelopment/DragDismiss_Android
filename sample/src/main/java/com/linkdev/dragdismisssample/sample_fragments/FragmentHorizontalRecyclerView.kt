package com.linkdev.dragdismisssample.sample_fragments

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linkdev.dragdismiss.DragDismiss
import com.linkdev.dragdismisssample.R
import com.linkdev.dragdismisssample.utils.Extras
import com.linkdev.dragdismisssample.utils.SampleDismissAttrs
import kotlinx.android.synthetic.main.fragment_horizontal_recycler_view.*
import kotlinx.android.synthetic.main.fragment_horizontal_recycler_view.toolbar

class FragmentHorizontalRecyclerView : Fragment() {

    companion object {
        const val TAG = "FragmentHorizontalRecyclerView"

        fun newInstance(sampleDismissAttrs: SampleDismissAttrs) =
            FragmentHorizontalRecyclerView().apply {
                arguments = Bundle().apply {
                    putParcelable(Extras.EXTRA_SAMPLE_ATTRS, sampleDismissAttrs)
                }
            }
    }

    private lateinit var mContext: Context

    private var mDataList = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getDragDismissView()
    }

    private fun getDragDismissView(): View {
        val sampleAttrs = arguments?.getParcelable<SampleDismissAttrs>(Extras.EXTRA_SAMPLE_ATTRS)!!
        return DragDismiss.create(requireActivity())
            .setDragScreenPercentage(sampleAttrs.dragDismissScreenPercentage)
            .setDragVelocityLevel(sampleAttrs.dragDragDismissVelocityLevel)
            .setDragDismissDirections(*sampleAttrs.draggingDirections.toTypedArray())
            .setDragBackgroundDimPercentage(sampleAttrs.backgroundDim)
            .attach(this, R.layout.fragment_horizontal_recycler_view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = requireActivity()

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.adapter = adapter
    }

    private val adapter: RecyclerView.Adapter<ViewHolder> =
        object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val textView = TextView(mContext)
                textView.layoutParams = RecyclerView.LayoutParams(500, 500)
                textView.gravity = Gravity.CENTER
                return ViewHolder(textView)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.tv.text = "${mDataList[position]}"
                holder.tv.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.colorAccent
                    )
                )
                if (mDataList[position] % 2 == 0) {
                    holder.tv.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext, R.color.colorPrimary
                        )
                    )
                } else {
                    holder.tv.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext, R.color.colorPrimaryDark
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