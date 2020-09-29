package com.linkdev.dragDismiss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.linkdev.android.dragdismiss.DragDismiss
import com.linkdev.dragDismiss.utils.Extras
import com.linkdev.dragDismiss.utils.SampleDismissAttrs

// Created by Mohammed Fareed on 9/28/2020.
// Copyright (c) 2020 Link Development All rights reserved.
class SampleFragment : Fragment(R.layout.fragment_sample) {

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
        val sampleAttrs = arguments?.getParcelable<SampleDismissAttrs>(Extras.EXTRA_SAMPLE_ATTRS)!!
        return DragDismiss.create(requireActivity())
            .setDragDismissScreenPercentage(sampleAttrs.dragDismissScreenPercentage)
            .setDragDismissVelocityLevel(sampleAttrs.dragDragDismissVelocityLevel)
            .setShouldDragEdgeOnly(sampleAttrs.shouldDragEdgeOnly)
            .setDragDismissDraggingDirections(sampleAttrs.draggingDirections)
            .setDragDismissBackgroundAlpha(sampleAttrs.backgroundAlpha)
            .attach(this, R.layout.fragment_sample)
    }
}
