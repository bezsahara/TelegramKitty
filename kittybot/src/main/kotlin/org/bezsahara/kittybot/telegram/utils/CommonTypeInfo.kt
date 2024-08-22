package org.bezsahara.kittybot.telegram.utils

import io.ktor.util.reflect.*
import kotlin.reflect.typeOf

internal object CommonTypeInfo {
    @JvmField
    val str = typeInfoImpl(String::class.java, String::class, typeOf<String>())
}