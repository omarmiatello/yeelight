@file:OptIn(ExperimentalTime::class)

package com.github.omarmiatello.yeelight

import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

fun YeelightApi.flowGreen(
    repeat: Int = 2,
    action: FlowEndAction = FlowEndAction.recover,
    speed: Double = 1.0,
) = easyFlow(
    flowTuples = listOf(
        FlowColor(0x00ff00, 100, 2.seconds * speed),
        FlowColor(0x00ff00, 30, 1.seconds * speed),
    ),
    repeat = repeat,
    action = action
)

fun YeelightApi.flowRed(
    repeat: Int = 2,
    action: FlowEndAction = FlowEndAction.recover,
    speed: Double = 1.0,
) = easyFlow(
    flowTuples = listOf(
        FlowColor(0xff0000, 100, 2.seconds * speed),
        FlowColor(0xff0000, 30, 1.seconds * speed),
    ),
    repeat = repeat,
    action = action
)

fun YeelightApi.flowPolice(
    repeat: Int = 2,
    action: FlowEndAction = FlowEndAction.recover,
    speed: Double = 1.0,
) = easyFlow(
    flowTuples = listOf(
        FlowColor(0xff0000, 100, 500.milliseconds * speed),
        FlowSleep(200.milliseconds * speed),
        FlowColor(0x0000ff, 100, 500.milliseconds * speed),
        FlowSleep(200.milliseconds * speed),
    ),
    repeat = repeat,
    action = action
)
