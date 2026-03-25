package org.bezsahara.kittybot.telegram.client.file


import java.io.File
import java.io.InputStream

fun InputStream.toTelegramFile(fileName: String? = null, contentType: String? = null): InputStreamAdapter {
    return object : InputStreamAdapter() {
        override val fileName: String
            get() = fileName ?: super.fileName
        override val contentType: String
            get() = contentType ?: super.contentType

        override fun provide(): InputStream {
            return this@toTelegramFile
        }
    }
}


fun File.toTelegramFile(): InputStreamFile {
    return InputStreamFile(this)
}

fun ByteArray.toTelegramFile(name: String? = null, contentType: String? = null): TelegramFileVertx.Bytes {
    return TelegramFileVertx.Bytes(this, name, contentType)
}

fun ByteArray.toTelegramFile(): TelegramFileVertx.Bytes {
    return TelegramFileVertx.Bytes(this, null, null)
}


