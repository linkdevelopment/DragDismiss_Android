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

/**
 * The velocity in Pixels/second that the view will be dismissed if flanged above.
 * The higher the level the higher the speed required to dismiss
 */
enum class DragDismissVelocityLevel(val velocity: Int) {
    LEVEL_0(0),
    LEVEL_1(1000),
    LEVEL_2(2000),
    LEVEL_3(3000),
    LEVEL_4(4000),
    LEVEL_5(5000),
}
