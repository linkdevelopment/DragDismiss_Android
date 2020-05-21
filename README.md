## DragDismiss
[![Platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)
[![API](https://img.shields.io/badge/Min--SDK-19-yellowgreen)]
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Many studies and surveys showed that users prefer dragging and swiping to clicking...

DragDismissLayout is a ViewGroup,  
**Built** using the ViewDragHelper utility class,  
**Developed** to help you give your users the flexibility of dragging.

- No changes in code required, Just wrap your xml View with DragDismissLayout and voila.
- We support all types of views static and scrolling horizontally or vertically.
- Works with out of the box with activities, And supports fragments following below steps.
- Built using androidx ViewDragHelper utility class.
- Written completely in kotlin supporting kotlin-Idiomatic.
- Tested and applied in multiple applications.

## Start using DragDismiss
`latestVersion` is ![](https://img.shields.io/badge/platform-android-brightgreen.svg)

Add the following in your app's `build.gradle` file:
```groovy
repositories {
    jcenter()
}
dependencies {
    implementation 'com.linkdev.android:dragdismiss:${latestVersion}'
}
```
First wrap the target layout with `DragDismissLayout` ViewGroup
```xml
<com.linkdev.android.dragdismiss.DragDismissLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/dragDismissLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:dragDismissDistanceFraction="50%"
    app:dragDismissVelocityLevel="level_3"
    app:draggingDirections="fromLeft|fromRight"
    app:maskAlpha="50"
    app:shouldDragEdgeOnly="false">

</com.linkdev.android.dragdismiss.DragDismissLayout>
```

#### With activities
Use `Theme.DragDismiss` as your activity's theme
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

#### With fragments
Due to the view destruction of the replaced Fragments, Fragments that are implementing DragDismissLayout has to be **added**.


## Contribute
Contributions and contributors are always welcome!, Help us make DragDismiss better and give back to the community.

Found an issue or feel like contributing? Please use [Github][issues]  
Have a question? Please use Stackoverflow with tag [DragDismissLayout][stackoverflow]

## License
    Copyright 2015 Link Development

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