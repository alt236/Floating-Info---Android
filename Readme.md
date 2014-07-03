Floating Info
=========

Floating Info is an Android application that displays the following in a system overlay window:

* The application name, package name and process id of the application which is currently in the device's foreground
* The Global CPU utilisation with a per-core breakdown - This is not foreground application specific.
* Memory usage breakdown for the currently foregrounded process (read the [notes](#notes) for the caveats).

<a href="https://play.google.com/store/apps/details?id=uk.co.alt236.floatinginfo">
  <img alt="Android app on Google Play"
       src="https://developer.android.com/images/brand/en_app_rgb_wo_60.png" />
</a>

## Sample App Screenshots

![screenshot1](https://github.com/alt236/Floating-Info---Android/raw/master/screenshots/screenshot_1.png)
![screenshot2](https://github.com/alt236/Floating-Info---Android/raw/master/screenshots/screenshot_2.png)
![screenshot3](https://github.com/alt236/Floating-Info---Android/raw/master/screenshots/screenshot_3.png)
![screenshot4](https://github.com/alt236/Floating-Info---Android/raw/master/screenshots/screenshot_4.png)

# <a name="notes"></a> Notes and Caveats
* The application will show the memory allocation of the currently foregrounded process which it gets by getting the Process Id of the currently foregrounded activity. This means that if an application has spawned multiple processes, this application will only show the memory utilisation of the main process.
* The Memory information displayed come via a [Debug.MemoryInfo](http://developer.android.com/reference/android/os/Debug.MemoryInfo.html) object, while using reflection to expose a number of hidden [fields](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/os/Debug.java).
* Data updates happen approximately every 1 second.

# Android Memory Usage
Memory management on Android is pretty complex and the easiest way to get started with understanding it is reading [this](https://developer.android.com/tools/debugging/debugging-memory.html) article - especially the "Viewing Overall Memory Allocations" section.

## Credits

Author: [Alexandros Schillings](https://github.com/alt236)

Based on [GhostLog](https://github.com/jgilfelt/GhostLog) by [Jeff Gilfelt](https://github.com/jgilfelt)

The icon was adapted from [this](http://www.clker.com/clipart-duck-silhouette.html) one.

## License

    Copyright (C) 2014 readyState Software Ltd

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.