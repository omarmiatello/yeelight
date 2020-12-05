package com.github.omarmiatello.yeelight

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

// Docs: http://www.yeelight.com/download/Yeelight_Inter-Operation_Spec.pdf
object YeelightApi {
    fun getProperties(vararg propertiesNames: String) =
        YeelightCmd("get_prop", propertiesNames.toList())

    fun setCurrentAsDefault() = YeelightCmd("set_default")
    fun setPower(isOn: Boolean = true, effect: SpeedEffect = SpeedEffect.smooth, duration: Int = 500) =
        YeelightCmd("set_power", listOf(if (isOn) "on" else "off", effect.id, duration))

    fun toggle() = YeelightCmd("toggle")

    /**
     * brightness: 1 - 100
     */
    fun setBrightness(brightness: Int, effect: SpeedEffect = SpeedEffect.smooth, duration: Int = 500) =
        YeelightCmd("set_bright", listOf(brightness.coerceIn(1..100), effect.id, duration))

    fun startColorFlow(count: Int, action: FlowEndAction, flowExpression: String) =
        YeelightCmd("start_cf", listOf(count, action.id, flowExpression))

    fun stopColorFlow() = YeelightCmd("stop_cf")

    fun _setScene(): YeelightCmd = TODO("Missing app prarams") // Cmd("set_scene")
    fun _cronAdd(): YeelightCmd = TODO("Missing app prarams") // Cmd("cron_add")
    fun _cronGet(): YeelightCmd = TODO("Missing app prarams") // Cmd("cron_get")
    fun _cronDel(): YeelightCmd = TODO("Missing app prarams") // Cmd("cron_del")

    /**
     * colorTemperature: 1700 ~ 6500
     */
    fun setWhiteTemperature(whiteTemperature: Int, effect: SpeedEffect = SpeedEffect.smooth, duration: Int = 500) =
        YeelightCmd("set_ct_abx", listOf(whiteTemperature.coerceIn(1700..6500), effect.id, duration))

    fun setColorRgb(color: Int, effect: SpeedEffect = SpeedEffect.smooth, duration: Int = 500) =
        YeelightCmd("set_rgb", listOf(color.coerceIn(0..0xffffff), effect.id, duration))
}

enum class SpeedEffect { sudden, smooth; val id = name } 
enum class FlowEndAction(val id: Int) { recover(0), stay(1), off(2) }
private enum class FlowMode(val id: Int) { colorRgb(1), colorTemperature(2), sleep(7) }


@ExperimentalTime
sealed class FlowTuple(
    val duration: Duration,
    private val mode: FlowMode,
    val value: Int,
    val brightness: Int
) {
    override fun toString() = "${duration.toLongMilliseconds()},${mode.id},$value,$brightness"
}

@ExperimentalTime
class FlowColor(
    color: Int,
    brightness: Int = 100,
    duration: Duration = 1.seconds,
) : FlowTuple(duration.coerceAtLeast(50.milliseconds), FlowMode.colorRgb, color.coerceIn(0x000000..0xFFFFFF), brightness.coerceIn(-1..100))

@ExperimentalTime
class FlowSleep(
    duration: Duration = 1.seconds
) : FlowTuple(duration.coerceAtLeast(50.milliseconds), FlowMode.sleep, 0, 0)

@ExperimentalTime
class FlowColorTemperature(
    color: Int,
    brightness: Int = 100,
    duration: Duration = 1.seconds,
) : FlowTuple(duration.coerceAtLeast(50.milliseconds), FlowMode.colorTemperature, color.coerceIn(1700..6500), brightness.coerceIn(-1..100))