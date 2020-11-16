[![](https://jitpack.io/v/Idan503/CustomProgressBar.svg)](https://jitpack.io/#Idan503/CustomProgressBar)
# CustomProgressBar
A simple and easy to use customizable progress bar view for Android.
#### Main Features:  
- Custom Title
    - Text that can be on left, center, or right side of the bar
    - Title can show either progress decimal value, percentage or a static text 
- Bar Colors
    - Static bar and background color  
    - Gradient bar of up to 3 colors
    - Dynamic color that changes gradiently matching bar value
- Value Change Animation
    - Option to apply smooth animation transition for a bar value change easily  


## Screenshots
<p float="left" align="middle" padding="5">
  <img src="/screenshots/CustomProgressBar1.png?raw=true" width="180" />
  <img src="/screenshots/CustomProgressBar2.png?raw=true" width="180" />
  <img src="/screenshots/CustomProgressBar3.png?raw=true" width="180" />
</p>

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

## Usage
XML implementation example:
```
<com.idankorenisraeli.customprogressbar.CustomProgressBar
	android:layout_width="match_parent"
	android:layout_height="50dp"

	app:barPadding="5dp"
	app:barCornerRadius="8dp"

	app:barBackgroundColor="@color/dark_gray"
	app:colorType="single_static"
	app:colorStatic="@color/white"

	app:textType="custom_static"
	app:textTitle="Hello World"
	app:textPadding="5dp"
	app:textGravity="center"
	app:textColor="@color/light_blue"
	
	app:value="0.35"
/>
```

## License

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