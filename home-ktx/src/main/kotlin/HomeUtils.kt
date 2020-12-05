package com.github.omarmiatello.yeelight.home

import com.github.omarmiatello.yeelight.YeelightManager

// lights

suspend fun YeelightManager.bagno() = findDeviceById("0x0000000012ab237b")!!
suspend fun YeelightManager.camera1() = findDeviceById("0x0000000012a895d7")!!
suspend fun YeelightManager.camera2() = findDeviceById("0x00000000123f06fc")!!
suspend fun YeelightManager.cucina1() = findDeviceById("0x0000000011744b6b")!!
suspend fun YeelightManager.cucina2() = findDeviceById("0x000000000795f4d8")!!
suspend fun YeelightManager.lavanderia() = findDeviceById("0x0000000012ab33cd")!!
suspend fun YeelightManager.soggiornoDivano1() = findDeviceById("0x00000000116ff5b8")!!
suspend fun YeelightManager.soggiornoDivano2() = findDeviceById("0x00000000116ffa39")!!
suspend fun YeelightManager.soggiornoDivano3() = findDeviceById("0x00000000047e92c2")!!
suspend fun YeelightManager.soggiornoFinestraCucina() = findDeviceById("0x0000000012a6695a")!!
suspend fun YeelightManager.soggiornoLiquori() = findDeviceById("0x00000000031b0f7c")!!
suspend fun YeelightManager.studio1() = findDeviceById("0x00000000052f0073")!!
suspend fun YeelightManager.studio2() = findDeviceById("0x00000000124dd1b7")!!
suspend fun YeelightManager.studio3() = findDeviceById("0x00000000117480a1")!!

// rooms

suspend fun YeelightManager.roomBagno() = listOf(bagno())
suspend fun YeelightManager.roomCamera() = listOf(camera1(), camera2())
suspend fun YeelightManager.roomCucina() = listOf(cucina1(), cucina2())
suspend fun YeelightManager.roomLavanderia() = listOf(lavanderia())
suspend fun YeelightManager.roomSoggiorno() =
    listOf(soggiornoDivano1(), soggiornoDivano2(), soggiornoDivano3(), soggiornoLiquori(), soggiornoFinestraCucina())

suspend fun YeelightManager.roomStudio() = listOf(studio1(), studio2(), studio3())

// all

suspend fun YeelightManager.roomAll() =
    roomBagno() + roomCamera() + roomCucina() + roomLavanderia() + roomSoggiorno() + roomStudio()
