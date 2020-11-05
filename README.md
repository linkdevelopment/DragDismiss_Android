# DragDismiss
[![Platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)
![API](https://img.shields.io/badge/Min--SDK-21-yellowgreen)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

DragDismissLayout is a ViewGroup, built using the ViewDragHelper utility class, To help you give your users the flexibility of swiping to dismiss the visible screen.

![](screenshots/screenshot.gif)

- We support all types of views static, scrolling horizontally or vertically.
- Works with activities and fragments.
- Built using androidx ViewDragHelper utility class.
- Kotlin-Idiomatic, written completely in kotlin.

#Setup

Gradle:
```
implementation 'com.linkdev.dragdismiss:dragdismiss:1.0.0'
```
Maven:
```
<dependency>
  <groupId>com.linkdev.dragdismiss</groupId>
  <artifactId>dragdismiss</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

# How to use DragDismiss
DragDismiss can be used with both activities and fragments:

##Activites
**Firstly,** you will have to use `Theme.DragDismiss` as your activity's theme
```xml
<activity
    android:name=".ActivityName"
    android:theme="@style/DragDismissTheme" />
```
Or add these to your existing Theme for activities that implement DragDismiss
```xml
<item name="android:windowFullscreen">true</item>
<item name="android:windowIsTranslucent">true</item>
<item name="android:windowBackground">@android:color/transparent</item>
```
**Then,** You can set the DragDismiss directly from your onCreate like below
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getDragDismissContentView())
}

fun getDragDismissContentView() =
    DragDismiss.create(mContext)
        .attach(this, R.layout.activity_layout_name)
```
The `DragDismiss.attach(...)` method takes the whole activity layout and returns it again ready to be Dragged and Dismissed
 so you should call `setContentView(...)` in your activity using this new View.

## Fragments
**Firstly, ** It's required that the fragment is added and not replaced using the Fragment manger like below.
```kotlin
supportFragmentManager.beginTransaction()
    .add(R.id.fragmentContainer, SampleFragment.newInstance(dragDismissAttrs), SampleFragment.TAG)
    .addToBackStack(null)
    .commit()
```

**Then, ** You can set the DragDismiss directly from your onCreateView like below.
```kotlin
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    return getDragDismissContentView()
}

private fun getDragDismissContentView(): View {
    return DragDismiss.create(requireActivity())
        .attach(this, R.layout.fragment_layout_name)
}
```
The `DragDismiss.attach(...)` method takes the whole fragment layout and returns it again ready to be Dragged and Dismissed
 set the view of your fragment using this new View.

#Custmizations
For extra custmizations for your DragDismissLayout check below.
```kotlin
DragDismiss.create(mContext)
    .setDragDismissScreenPercentage(40)
    .setDragDismissVelocityLevel(DragDismissVelocityLevel.LEVEL_3)
    .setDragDismissDraggingDirections(DragDismissDirections.FROM_LEFT, DragDismissDirections.FROM_RIGHT)
    .setDragDismissBackgroundDim(80)
    .attach(this, R.layout.activity_layout_name)
```

## Set screen dismiss percentage
The percentage of the screen traveled before the screen is dismissed on release.
```kotlin
DragDismiss.create(mContext)
    .setDragDismissScreenPercentage(40)
```
**Default: ** 40

## Set velocity level
The Speed that if the screen is flung past it will be dismissed.
Possible values are
* LEVEL_0
* LEVEL_1
* LEVEL_2
* LEVEL_3
* LEVEL_4
* LEVEL_5

The higher the level the harder it is to dismiss the screen by a fling.

```kotlin
DragDismiss.create(mContext)
    .setDragDismissVelocityLevel(DragDismissVelocityLevel.LEVEL_3)
```
**Default: ** LEVEL_3

**Note, ** LEVEL_0 disables the dismiss feature

## Set dragging directions
The directions that the screen can be dragged from, Possible values:
* ALL
* FROM_LEFT
* FROM_RIGHT
* FROM_TOP
* FROM_BOTTOM
```kotlin
DragDismiss.create(mContext)
    .setDragDismissDraggingDirections(DragDismissDirections.FROM_LEFT , DragDismissDirections.FROM_RIGHT)
```
**Default: ** FROM_LEFT

**Note, ** For the time being, If your view contains a view pager, avoid using it with direction `All` as it will interfere with the view pager scrolling behavior.

## Set dragging background dim
While dragging you could have a black transparent window show above the previous screen that gets brighter when the screen is about to be dismiss.
This attribute sets the percentage of the dim of the previous screen while dragging, The higher the darker.
```kotlin
DragDismiss.create(mContext)
    .setDragDismissBackgroundDim(80)
```
**Default: ** 80

**Note, ** set to 0 to make it fully visible.

You can learn more and play around with the attrs in the sample by cloning the repo.

# Contribute
Contributions and contributors are always welcome! Help us make DragDismiss better and give back to the community.

Found an issue or feel like contributing? Please use [Github][issues]  
Have a question? Please use Stackoverflow with tag [DragDismissLayout][stackoverflow]

# License
    Copyright 2020-present Link Development

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [issues]: https://github.com/DragDismissLayout/issues
 [stackoverflow]: http://stackoverflow.com/questions/tagged/DragDismissLayout
 