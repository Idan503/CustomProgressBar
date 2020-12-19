

[![](https://jitpack.io/v/Idan503/CustomProgressBar.svg)](https://jitpack.io/#Idan503/CustomProgressBar) [![GitHub license](https://img.shields.io/github/license/Idan503/CustomProgressBar?style=flat-square)](https://github.com/Idan503/CustomProgressBar)
# CustomProgressBar
A simple and easy to use customizable progress bar view for Android.
#### Main Features:  
- Custom Title
    - Text that can be on the left, center, or right side of the bar
    - Title can show either progress decimal value, percentage, or a static text 
- Bar Colors
    - Static bar and background color  
    - Gradient bar of up to 3 colors
    - Dynamic color that changes as a gradient, matching the bar value
- Value Change Animation
    - Option to apply smooth animation transition for a bar value change easily  


## Screenshots

</br>
<img src="/screenshots/bar_dynamic_animation.gif" width="485" height="75"> </br>

## Usage
Here is an example of implementation in an XML file:
```
<com.idankorenisraeli.customprogressbar.CustomProgressBar
    android:layout_width="match_parent"
    android:layout_height="50dp"

    app:barPadding="5dp"
    app:barCornerRadius="50"

    app:barBackgroundColor="@color/dark_gray"
    app:colorType="gradient"
    app:colorStart="@color/light_gray"
    app:colorEnd="@color/lighter_gray"

    app:textType="custom_static"
    app:textTitle="Hello World"
    app:textPadding="5dp"
    app:textGravity="center"
    app:textColor="@color/text_blue"

    app:value="0.8"
/>
```

<img src="/screenshots/CustomProgressBar4.png" width="350" height="58"></br>
The same result can be achieved similarly programmatically, a full example can be found in README_LONG.md file

## Setup
##### Step 1
Add this to build.gradle of your project
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

##### Step 2
Add this dependency to your build.gradle of your app
```
dependencies {
	implementation 'com.github.Idan503:CustomProgressBar:<version>'
}
```	

## License

```
Copyright 2020 Idan Koren-Israeli

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