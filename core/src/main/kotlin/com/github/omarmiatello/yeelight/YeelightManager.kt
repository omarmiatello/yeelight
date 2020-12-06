package com.github.omarmiatello.yeelight

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import java.net.InetSocketAddress
import kotlin.io.use
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
class YeelightManager(
    private val enableLog: Boolean = true,
) {
    private val devicesCache: MutableSet<YeelightDevice> = mutableSetOf()

    suspend fun findAllDevicesFlow(timeout: Duration = 2.seconds): Flow<YeelightDevice> = flow {
        withTimeoutOrNull(timeout) {
            aSocket(ioSelector).udp().bind().use { socket ->
                socket.send(
                    Datagram(
                        buildPacket { writeText("M-SEARCH * HTTP/1.1\r\nMAN:\"ssdp:discover\"\r\nST:wifi_bulb\r\n") },
                        InetSocketAddress("239.255.255.250", 1982)
                    )
                )
                socket.incoming.consumeEach {
                    it.packet.readText()
                        .also { response -> if (enableLog) println(response) }
                        .toYeelightBulb()
                        .also { device ->
                            devicesCache += device
                            emit(device)
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
        val device by lazy { devicesCache.firstOrNull { it.id == id } }
        return if (forceRefresh || device == null) {
            findAllDevicesFlow(timeout).firstOrNull { it.id == id }
        } else device
    }

    suspend fun findAllDevices(timeout: Duration = 2.seconds): Set<YeelightDevice> {
        findAllDevicesFlow(timeout).collect()
        return devicesCache
    }

    fun clearCache() = devicesCache.clear()

    suspend fun sendToAllDevices(cmd: YeelightCmd): List<String?> = coroutineScope {
        findAllDevices()
        devicesCache.map { async { it.send(cmd) } }.awaitAll()
    }

    suspend fun printAllYeelightDevices() = findAllDevices()
        .forEachIndexed { index, device ->
            val power = if (device.power) "on" else "off"
            println("${index + 1}\t$power\t${device.name} id: ${device.id} bright: ${device.bright} -- $device")
        }

    companion object {
        val ioSelector = ActorSelectorManager(Dispatchers.IO)
    }
}


