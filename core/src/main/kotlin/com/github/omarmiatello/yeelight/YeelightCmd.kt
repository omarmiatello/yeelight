package com.github.omarmiatello.yeelight

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

private val json = Json {
    encodeDefaults = true
    serializersModule = SerializersModule {
        contextual(String.serializer())
        contextual(Int.serializer())
    }
}

@Serializable
data class YeelightCmd(val method: String, val params: List<@Contextual Any> = emptyList()) {
    val id = 1

    @Transient val realCommand = json.encodeToString(this)
}