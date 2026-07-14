package org.bezsahara.kittybot.telegram.client.file

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import org.bezsahara.kittybot.telegram.client.CustomRequest

fun createBoundary(): String {
    return generateMultiPartBoundary()
}

fun mpContentType(boundary: String): String {
    return "multipart/form-data; boundary=$boundary"
}

class CustomMPB(val customRequest: CustomRequest, val boundary: String) {
    suspend fun writeSuspend(bytes: ByteArray) {
        customRequest.writeSuspend(bytes, 0, bytes.size)
    }

    suspend fun writeSuspend(bytes: ByteArray, startIndex: Int, endIndex: Int) {
        customRequest.writeSuspend(bytes, startIndex, endIndex)
    }

    fun write(bytes: ByteArray) {
        customRequest.write(bytes, 0, bytes.size)
    }

    fun write(bytes: ByteArray, startIndex: Int, endIndex: Int) {
        customRequest.write(bytes, startIndex, endIndex)
    }

    fun beginPart(attributes: String) {
        customRequest.write("--$boundary\r\nContent-Disposition: form-data; $attributes\r\n\r\n")
    }

    fun endPart() {
        customRequest.write("\r\n")
    }

    fun writePart(data: String, attributes: String) {
        customRequest.write("--$boundary\r\nContent-Disposition: form-data; $attributes\r\n\r\n$data\r\n")
    }

    fun writeNormalPart(name: String, value: String) {
        customRequest.write("--$boundary\r\nContent-Disposition: form-data; name=$name\r\n\r\n")
        customRequest.write(value)
        customRequest.write("\r\n")
    }

    fun <T> writeJsonPart(name: String, serializer: SerializationStrategy<T>, value: T, json: Json) {
        customRequest.write("--$boundary\r\nContent-Disposition: form-data; name=$name\r\n\r\n")
        customRequest.write(json.encodeToString(serializer, value))
        customRequest.write("\r\n")
    }

    fun finish() {
        customRequest.write("--$boundary--\r\n")
    }
}
