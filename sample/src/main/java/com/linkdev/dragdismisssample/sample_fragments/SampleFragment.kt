package com.linkdev.dragdismisssample.sample_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.linkdev.dragdismiss.DragDismiss
import com.linkdev.dragdismisssample.R
import com.linkdev.dragdismisssample.utils.Extras
import com.linkdev.dragdismisssample.utils.SampleDismissAttrs

// Copyright (c) 2020 Link Development All rights reserved.
class SampleFragment : Fragment() {

    companion object {
        const val TAG = "SampleFragment"

        fun newInstance(dragDismissAttrs: SampleDismissAttrs) = SampleFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Extras.EXTRA_SAMPLE_ATTRS, dragDismissAttrs)
            }
        }
    }

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
            .attach(this, R.layout.fragment_sample)
    }
}
