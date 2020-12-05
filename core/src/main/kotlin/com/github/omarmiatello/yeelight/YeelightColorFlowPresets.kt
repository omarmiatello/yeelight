@file:OptIn(ExperimentalTime::class)

package com.github.omarmiatello.yeelight

import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

fun flowGreen(speed: Double = 1.0) = listOf(
    FlowColor(0x00ff00, 100, 2.seconds * speed),
    FlowColor(0x00ff00, 30, 1.seconds * speed),
)

fun flowRed(speed: Double = 1.0) = listOf(
    FlowColor(0xff0000, 100, 2.seconds * speed),
    FlowColor(0xff0000, 30, 1.seconds * speed),
)

fun flowPolice(speed: Double = 1.0) = listOf(
    FlowColor(0xff0000, 100, 500.milliseconds * speed),
    FlowSleep(200.milliseconds * speed),
    FlowColor(0x0000ff, 100, 500.milliseconds * speed),
    FlowSleep(200.milliseconds * speed),
)
