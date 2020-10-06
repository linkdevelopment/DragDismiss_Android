package com.linkdev.dragDismiss.utils

import android.os.Parcelable
import com.linkdev.android.dragdismiss.models.DragDismissVelocityLevel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SampleDismissAttrs(
    val dragDismissScreenPercentage: Float,
    val dragDragDismissVelocityLevel: DragDismissVelocityLevel,
    val shouldDragEdgeOnly: Boolean,
    val draggingDirections: Int,
    val backgroundAlpha: Float
) : Parcelable
