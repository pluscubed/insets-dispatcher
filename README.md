# Insets Dispatcher

[![Release](https://jitpack.io/v/com.pluscubed/insets-dispatcher.svg)](https://jitpack.io/#com.pluscubed/insets-dispatcher)  [![License](https://img.shields.io/github/license/pluscubed/insets-dispatcher.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Easily use window insets padding (e.g. status and navigation bars)

Documentation is being updated - check out [Polar Dashboard](https://github.com/afollestad/polar-dashboard) for sample use in a project.

### Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Add this to your module's `build.gradle` file:

```Gradle
dependencies {
	...
	compile 'com.pluscubed:insets-dispatcher:{latest-version}'
}
```

The library is versioned according to [Semantic Versioning](http://semver.org/).

### Usage

The InsetsDispatcher layouts take the window insets and set the padding/margin of child views according to layout attributes. It uses window insets on 5.0+ and insets given by `fitsSystemWindows` on 4.x.

On 4.x, no other views can have `fitsSystemWindows="true"` because they would consume the insets. Ideally the dispatcher layout would be the root layout, and dispatcher layouts would be nested until the view that needs insets applied is reached.

The dispatcher layouts will also work for Fragments, as long as the Fragment root view is a dispatcher layout.

The layout/view attributes are:
- `(layout_)windowInsets` - _flag_ - top, bottom
- `(layout_)windowInsetsUseMargin` - _boolean_ - default false (use padding)

The `layout_` attributes are used for child views of the dispatcher layouts. The normal view attributes can be used to apply insets to the dispatcher layouts themselves. 

These attributes must be applied under this package's namespace - i.e. `xmlns:app="http://schemas.android.com/apk/res-auto"` to let Android decide.

####Basic Example:

```xml
<com.pluscubed.insetsdispatcher.view.InsetsDispatcherLinearLayout
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="?colorPrimary"
        android:elevation="4dp"
        android:orientation="vertical"
        app:layout_windowInsets="top">

        <android.support.v7.widget.Toolbar
            ... />
            
        <android.support.design.widget.TabLayout
            ... />

    </LinearLayout>

</com.pluscubed.insetsdispatcher.view.InsetsDispatcherLinearLayout>

```
This would apply the top inset as a padding to the `LinearLayout`.

### License

```
Copyright 2015 Daniel Ciao

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
