package com.linkdev.dragdismisssample.sample_fragments.viewpager_sample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.linkdev.dragdismiss.DragDismiss
import com.linkdev.dragdismisssample.R
import com.linkdev.dragdismisssample.utils.Extras
import com.linkdev.dragdismisssample.utils.SampleDismissAttrs
import kotlinx.android.synthetic.main.fragment_viewpager.*

// Copyright (c) 2020 Link Development All rights reserved.
class FragmentViewPager : Fragment() {

    companion object {
        const val TAG = "FragmentViewPager"

        fun newInstance(sampleDismissAttrs: SampleDismissAttrs) =
            FragmentViewPager().apply {
                arguments = Bundle().apply {
                    putParcelable(Extras.EXTRA_SAMPLE_ATTRS, sampleDismissAttrs)
                }
            }
    }

    private lateinit var mContext: Context

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
            .setDragDismissDirections(sampleAttrs.draggingDirections)
            .setDragBackgroundDimPercentage(sampleAttrs.backgroundDim)
            .attach(this, R.layout.fragment_viewpager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = requireActivity()

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        initViewPager()
    }

    private fun initViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = viewPagerAdapter
        tablayout.setupWithViewPager(viewPager)
    }
}
