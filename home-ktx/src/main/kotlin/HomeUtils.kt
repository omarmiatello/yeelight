package com.github.omarmiatello.yeelight.home

import com.github.omarmiatello.yeelight.YeelightManager
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

const val ID_STUDIO_1 = "0x00000000052f0073"
const val ID_STUDIO_2 = "0x00000000124dd1b7"
const val ID_STUDIO_3 = "0x00000000117480a1"
const val ID_CUCINA_1 = "0x0000000011744b6b"
const val ID_CUCINA_2 = "0x000000000795f4d8"
const val ID_SOGGIORNO_LIQUORI = "0x00000000031b0f7c"
const val ID_SOGGIORNO_FINESTRA_CUCINA = "0x0000000012a6695a"
const val ID_SOGGIORNO_DIVANO_1 = "0x00000000116ff5b8"
const val ID_SOGGIORNO_DIVANO_2 = "0x00000000116ffa39"
const val ID_SOGGIORNO_DIVANO_3 = "0x00000000047e92c2"
const val ID_CAMERA_1 = "0x0000000012a895d7"
const val ID_CAMERA_2 = "0x00000000123f06fc"
const val ID_LAVANDERIA = "0x0000000012ab33cd"
const val ID_BAGNO = "0x0000000012ab237b"


suspend fun YeelightManager.studio1() = findDeviceById(ID_STUDIO_1)!!
suspend fun YeelightManager.studio2() = findDeviceById(ID_STUDIO_2)!!
suspend fun YeelightManager.studio3() = findDeviceById(ID_STUDIO_3)!!
suspend fun YeelightManager.cucina1() = findDeviceById(ID_CUCINA_1)!!
suspend fun YeelightManager.cucina2() = findDeviceById(ID_CUCINA_2)!!
suspend fun YeelightManager.soggiornoLiquori() = findDeviceById(ID_SOGGIORNO_LIQUORI)!!
suspend fun YeelightManager.soggiornoFinestraCucina() = findDeviceById(ID_SOGGIORNO_FINESTRA_CUCINA)!!
suspend fun YeelightManager.soggiornoDivano1() = findDeviceById(ID_SOGGIORNO_DIVANO_1)!!
suspend fun YeelightManager.soggiornoDivano2() = findDeviceById(ID_SOGGIORNO_DIVANO_2)!!
suspend fun YeelightManager.soggiornoDivano3() = findDeviceById(ID_SOGGIORNO_DIVANO_3)!!
suspend fun YeelightManager.camera1() = findDeviceById(ID_CAMERA_1)!!
suspend fun YeelightManager.camera2() = findDeviceById(ID_CAMERA_2)!!
suspend fun YeelightManager.lavanderia() = findDeviceById(ID_LAVANDERIA)!!
suspend fun YeelightManager.bagno() = findDeviceById(ID_BAGNO)!!

suspend fun YeelightManager.roomStudio() = listOf(studio1(), studio2(), studio3())
suspend fun YeelightManager.roomCamera() = listOf(camera1(), camera2())
suspend fun YeelightManager.roomCucina() = listOf(cucina1(), cucina2())
suspend fun YeelightManager.roomSoggiorno() = listOf(
    soggiornoLiquori(), soggiornoFinestraCucina(), soggiornoDivano1(),
    soggiornoDivano2(), soggiornoDivano3()
)

suspend fun YeelightManager.roomLavanderia() = listOf(lavanderia())
suspend fun YeelightManager.roomBagno() = listOf(bagno())

suspend fun YeelightManager.roomAll() = roomStudio() + roomCamera() + roomCucina() +
        roomSoggiorno() + roomLavanderia() + roomBagno()
