/**
 * Copyright (c) 2020-present Link Development
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkdev.android.dragdismiss.models

import com.linkdev.android.dragdismiss.models.DragDismissDirections
import com.linkdev.android.dragdismiss.models.DragDismissVelocityLevel

// Copyright (c) 2020 Link Development All rights reserved.
object DragDismissDefaults {
    const val DEFAULT_DISMISS_SCREEN_PERCENTAGE = 0.4f
    val DEFAULT_DISMISS_VELOCITY_LEVEL = DragDismissVelocityLevel.LEVEL_3
    const val DEFAULT_BACKGROUND_ALPHA_FRACTION = 0.8f
    const val DEFAULT_SHOULD_DRAG_EDGE_ONLY = false
    const val DEFAULT_DRAG_DIRECTION: Int = DragDismissDirections.DIRECTION_FROM_LEFT
}
