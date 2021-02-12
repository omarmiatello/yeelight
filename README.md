# Yeelight Kotlin library

Control your Xiaomi Yeelight lamp using Kotlin. This library use [Ktor](https://github.com/ktorio/ktor) for sockets, more info [available here](https://ktor.io/docs/servers-raw-sockets.html).

[![](https://jitpack.io/v/omarmiatello/yeelight.svg)](https://jitpack.io/#omarmiatello/yeelight)

This library has 3 modules:
- Module `:core:`
  - high level api: `YeelightManager`, `YeelightDevice`, `YeelightColorFlowPresets`
  - low level api: `YeelightApi`
- Module `:home-ktx:`
  - extensions (for my home)
- Module `:app:`
  - example app!


## Setup

Add this in your root `build.gradle` file:
```gradle
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```

Grab via Gradle (v4 or later):

Gradle Groovy syntax
```groovy
// core library
implementation 'com.github.omarmiatello.yeelight:core:1.0.3'

// extensions example (my home)
implementation 'com.github.omarmiatello.yeelight:home-ktx:1.0.3'
```

Gradle Kotlin syntax
```kotlin
// core library
implementation("com.github.omarmiatello.yeelight:core:1.0.3")

// extensions example (my home)
implementation("com.github.omarmiatello.yeelight:home-ktx:1.0.3")
```

### Example

```kotlin
suspend fun main() {
  val yeelight = YeelightManager()

  // for debug: Network search + print details
  yeelight.printAllYeelightDevices()

  val myDevice = yeelight.findDeviceById("0x0000000011744b6b")

  if (myDevice != null) {

    // switch on (smooth, 500ms)
    myDevice.setPower(true)

    // switch off (instant)
    myDevice.setPower(isOn = false, effect = SpeedEffect.sudden)

    // get properties values (ex: power state)
    myDevice.getProperties("power")

    // set current configuration as default
    myDevice.setCurrentAsDefault()

    // switch toggle `on -> off` or `off -> on`
    myDevice.toggle()

    // set brightness: 1 to 100
    myDevice.setBrightness(50)

    // start color flow
    myDevice.startColorFlow(
      flowTuples = listOf(FlowColor(0xFF0000), FlowColor(0x00FF00)),
      repeat = 1,
      action = FlowEndAction.off
    )

    // stop color flow
    myDevice.stopColorFlow()

    // set the smart LED directly to specified state.
    // If the smart LED is off, then it will turn on the smart LED firstly and then apply the specified command 
    myDevice.setScene(SceneAutoDelayOff(brightness = 50, duration = 3.minutes))

    // start a timer job
    myDevice.cronAdd(CronPowerOff(5.minutes))

    // retrieve the setting of the current cron job
    myDevice.cronGet()

    //  stop the specified cron job (currently support only CronPowerOff)
    myDevice.cronDel()

    // set white temperature (1700 - 5600)
    myDevice.setWhiteTemperature(5000)

    // set color from black `0x000000` to white `0xFFFFFF`
    myDevice.setColorRgb(0xFF0000)
  }

  // control a group of lights
  yeelight.roomCucina().forEach {
    it.setPower(true)
    it.startColorFlow(flowPolice(speed = 1.5), repeat = 3, action = FlowEndAction.off)
  }

  // preset flows
  yeelight.cucina1().startColorFlow(flowPolice())

  // custom flows
  yeelight.cucina2().startColorFlow(christmasUsaFlow)

  coroutineContext.cancel()
}

val christmasUsaFlow = listOf(
  FlowColor(0xFF0000, 100, 0.milliseconds),
  FlowSleep(300.milliseconds),
  FlowColor(0x0000FF, 100, 0.milliseconds),
  FlowSleep(300.milliseconds),
  FlowColor(0xFFFFFF, 100, 0.milliseconds),
  FlowSleep(300.milliseconds),
)
```

### Note: All functions here are ![suspend](docs/suspend.png) suspending function

[Kotlin suspending function](https://kotlinlang.org/docs/reference/coroutines/basics.html) does not block a thread, but suspends the coroutine, and it can be only used from a coroutine.

![Suspending functions](docs/suspending_functions.png)



## License

    MIT License
    
    Copyright (c) 2020 Omar Miatello
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
