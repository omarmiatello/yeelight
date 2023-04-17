@file:OptIn(ExperimentalTime::class)

package demo

import com.github.omarmiatello.yeelight.CronPowerOff
import com.github.omarmiatello.yeelight.FlowColor
import com.github.omarmiatello.yeelight.FlowEndAction
import com.github.omarmiatello.yeelight.FlowSleep
import com.github.omarmiatello.yeelight.SceneAutoDelayOff
import com.github.omarmiatello.yeelight.SpeedEffect
import com.github.omarmiatello.yeelight.YeelightManager
import com.github.omarmiatello.yeelight.flowPolice
import com.github.omarmiatello.yeelight.home.cucina1
import com.github.omarmiatello.yeelight.home.cucina2
import com.github.omarmiatello.yeelight.home.roomCucina
import kotlinx.coroutines.cancel
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

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

        // start flow
        myDevice.startColorFlow(
            flowTuples = listOf(FlowColor(0xFF0000), FlowColor(0x00FF00)),
            repeat = 1,
            action = FlowEndAction.off
        )

        // stop flow
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

val christmasFlow = listOf(
    FlowColor(0xFF0000),
    FlowColor(0x00FF00),
    FlowColor(0x0000FF),
    FlowColor(0xFFFF00),
    FlowColor(0xFF00FF),
    FlowColor(0x00FFFF),
    FlowColor(0xFFFFFF),
)

val christmasUsaFlow = listOf(
    FlowColor(0xFF0000, 100, 0.milliseconds),
    FlowSleep(300.milliseconds),
    FlowColor(0x0000FF, 100, 0.milliseconds),
    FlowSleep(300.milliseconds),
    FlowColor(0xFFFFFF, 100, 0.milliseconds),
    FlowSleep(300.milliseconds),
)