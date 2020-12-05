package com.github.omarmiatello.yeelight

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
class YeelightManager(
    private val enableLog: Boolean = true,
) {
    private val devices: MutableSet<YeelightDevice> = mutableSetOf()

    suspend fun findAllDevicesFlow(timeout: Duration = 1.seconds): Flow<YeelightDevice> = flow {
        withTimeout(timeout) {
            DatagramSocket().use { socket ->
                val msg = "M-SEARCH * HTTP/1.1\r\nMAN:\"ssdp:discover\"\r\nST:wifi_bulb\r\n".toByteArray()
                socket.send(DatagramPacket(msg, msg.size, InetAddress.getByName("239.255.255.250"), 1982))
                socket.soTimeout = timeout.toLongMilliseconds().toInt()
                while (true) {
                    if (!coroutineContext.isActive) return@use
                    try {
                        val response = DatagramPacket(ByteArray(1024), 1024)
                            .also { socket.receive(it) }
                            .data
                            .filter { it.toInt() != 13 }
                            .let { String(it.toByteArray()) }
                        if (enableLog) println(response)
                        val device = response.toYeelightBulb()
                        devices += device
                        emit(device)
                    } catch (e: SocketTimeoutException) {
                        // ignore
                    }
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun findDeviceById(
        id: String,
        timeout: Duration = 2.seconds,
        forceRefresh: Boolean = false
    ): YeelightDevice? {
        val device by lazy { devices.firstOrNull { it.id == id } }
        return if (forceRefresh || device == null) {
            findAllDevicesFlow(timeout).firstOrNull { it.id == id }
        } else device
    }

    suspend fun findAllDevices(timeout: Duration = 1.seconds): Set<YeelightDevice> {
        devices.clear()
        findAllDevicesFlow(timeout).collect()
        return devices
    }

    suspend fun sendToAllDevices(cmd: YeelightCmd): List<String?> = coroutineScope {
        findAllDevices()
        devices.map { async { it.send(cmd) } }.awaitAll()
    }

    suspend fun printAllYeelightDevices() {
        val allDevices = findAllDevices(2.seconds)
        allDevices.forEachIndexed { index, device ->
            val power = if (device.power) "on" else "off"
            println("${index + 1}\t$power\t${device.name} id: ${device.id} bright: ${device.bright} -- $device")
        }
    }
}


