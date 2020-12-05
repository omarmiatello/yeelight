package com.github.omarmiatello.yeelight

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.net.Socket
import java.net.SocketTimeoutException

data class YeelightDevice(
    val id: String,
    val ip: String,
    val port: Int,
    val model: String,
    val fw_ver: Int,
    val support: List<String>,
    val power: Boolean,
    val bright: String,
    val color_mode: Int,
    val ct: Int,
    val rgb: Int,
    val hue: Int,
    val sat: Int,
    val name: String,
    private val enableLog: Boolean = true,
) {
    suspend fun asyncCmd(cmd: YeelightCmd) = coroutineScope {
        async { sendCmd(cmd) }
    }

    suspend fun sendCmd(cmd: YeelightCmd): String? = withContext(Dispatchers.IO) {
        Socket(ip, port).use { socket ->
            // socket.setKeepAlive(true)
            socket.soTimeout = 1000
            socket.getOutputStream().bufferedWriter().use { writer ->
                if (enableLog) println("$id --> ${cmd.realCommand}")
                writer.write("${cmd.realCommand}\r\n")
                writer.flush()

                socket.getInputStream().bufferedReader().use { reader ->
                    try {
                        reader.readLine().also { if (enableLog) println("$id <-- $it") }
                    } catch (e: SocketTimeoutException) {
                        // ignore
                        null
                    }
                }
            }
        }
    }
}


fun String.toYeelightBulb(): YeelightDevice {
    val info = lines().map { it.split(":", limit = 2) }.filter { it.size == 2 }
        .map { it[0] to it[1].trim() }.toMap()
    val address = info.getValue("Location").split("//")[1].split(":")
    return YeelightDevice(
        id = info.getValue("id"),
        ip = address[0],
        port = address[1].toInt(),
        model = info.getValue("model"),
        fw_ver = info.getValue("fw_ver").toInt(),
        support = info["support"]!!.split(" "),
        power = info.getValue("power") == "on",
        bright = info.getValue("bright"),
        color_mode = info.getValue("color_mode").toInt(),
        ct = info.getValue("ct").toInt(),
        rgb = info.getValue("rgb").toInt(),
        hue = info.getValue("hue").toInt(),
        sat = info.getValue("sat").toInt(),
        name = info.getValue("name"),
    )
}