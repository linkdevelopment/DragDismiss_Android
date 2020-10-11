package com.linkdev.dragDismiss.sample_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.linkdev.android.dragdismiss.DragDismiss
import com.linkdev.dragDismiss.R
import com.linkdev.dragDismiss.utils.Extras
import com.linkdev.dragDismiss.utils.SampleDismissAttrs

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
            .setDragDismissScreenPercentage(sampleAttrs.dragDismissScreenPercentage)
            .setDragDismissVelocityLevel(sampleAttrs.dragDragDismissVelocityLevel)
            .setDragDismissDraggingDirections(sampleAttrs.draggingDirections)
            .setDragDismissBackgroundDim(sampleAttrs.backgroundAlpha)
            .attach(this, R.layout.fragment_sample)
    }
}
