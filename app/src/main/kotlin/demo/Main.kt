@file:OptIn(ExperimentalTime::class)

package demo

import com.github.omarmiatello.yeelight.*
import com.github.omarmiatello.yeelight.home.cucina1
import com.github.omarmiatello.yeelight.home.cucina2
import com.github.omarmiatello.yeelight.home.roomCucina
import com.github.omarmiatello.yeelight.home.roomLavanderia
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

suspend fun main() {
    val yeelight = YeelightManager()

    yeelight.roomCucina().forEach { it.sendCmd(YeelightApi.setPower(true)) }
    yeelight.cucina1().sendCmd(YeelightApi.flowPolice())
    yeelight.cucina2().sendCmd(christmasUsaFlow)

    coroutineContext.cancel()
}

val christmasFlow = YeelightApi.easyFlow(
    flowTuples = listOf(
        FlowColor(0xFF0000),
        FlowColor(0x00FF00),
        FlowColor(0x0000FF),
        FlowColor(0xFFFF00),
        FlowColor(0xFF00FF),
        FlowColor(0x00FFFF),
        FlowColor(0xFFFFFF),
    ),
)
val christmasUsaFlow = YeelightApi.easyFlow(
    flowTuples = listOf(
        FlowColor(0xFF0000, 100, 0.milliseconds),
        FlowSleep(300.milliseconds),
        FlowColor(0x0000FF, 100, 0.milliseconds),
        FlowSleep(300.milliseconds),
        FlowColor(0xFFFFFF, 100, 0.milliseconds),
        FlowSleep(300.milliseconds),
    ),
    repeat = 3,
    action = FlowEndAction.off,
)