package com.linkdev.dragDismiss.utils

import android.os.Parcelable
import com.linkdev.android.dragdismiss.DragDismissLayout
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SampleDismissAttrs(
    val dragDismissFactor: Float,
    val dragDismissVelocityLevel: DragDismissLayout.DismissVelocityLevel,
    val shouldDragEdgeOnly: Boolean,
    val draggingDirections: Int
) : Parcelable
