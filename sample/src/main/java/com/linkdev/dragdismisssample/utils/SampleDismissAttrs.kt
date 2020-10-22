package com.linkdev.dragdismisssample.utils

import android.os.Parcelable
import com.linkdev.dragdismiss.models.DragDismissDirections
import com.linkdev.dragdismiss.models.DragDismissVelocityLevel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SampleDismissAttrs(
    val dragDismissScreenPercentage: Int,
    val dragDragDismissVelocityLevel: DragDismissVelocityLevel,
    val draggingDirections: ArrayList<DragDismissDirections>,
    val backgroundDim: Int
) : Parcelable
