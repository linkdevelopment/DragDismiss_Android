package com.linkdev.dragDismiss.utils

import android.os.Parcelable
import com.linkdev.android.dragdismiss.models.DragDismissVelocityLevel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SampleDismissAttrs(
    val dragDismissScreenPercentage: Int,
    val dragDragDismissVelocityLevel: DragDismissVelocityLevel,
    val draggingDirections: Int,
    val backgroundAlpha: Int
) : Parcelable
