package com.linkdev.dragDismiss.utils

import com.linkdev.android.dragdismiss.DragDismissLayout

fun DragDismissLayout.applyAttrs(sampleAttrs: SampleDismissAttrs) {
    setDragDismissDistance(sampleAttrs.dragDismissFactor)
    setDragDismissVelocityLevel(sampleAttrs.dragDismissVelocityLevel)
    setEdgeEnabled(sampleAttrs.shouldDragEdgeOnly)
    setDragDirections(sampleAttrs.draggingDirections)
    setBackgroundAlpha(sampleAttrs.backgroundAlpha)
}
