package org.bezsahara.kittybot.bot.json

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

fun JsonObject.toDescriptor(name: String): SerialDescriptor {
    val jo = this
    return buildClassSerialDescriptor(name) {
        jo.forEach { (string, element) ->
            val d = if (element is JsonPrimitive) {
                if (element.isString) {
                    String.serializer().descriptor
                } else if (element.longOrNull != null) {
                    Long.serializer().descriptor
                } else if (element.doubleOrNull != null) {
                    Double.serializer().descriptor
                } else error("Cannot create descriptor from $element")
            } else error("Cannot create descriptor from $element")

            element(string, d)
        }
    }
}