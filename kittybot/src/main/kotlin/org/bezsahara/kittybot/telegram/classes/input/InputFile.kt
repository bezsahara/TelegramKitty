package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents the contents of a file to be uploaded. Must be posted using multipart/form-data in the usual way that files are uploaded via the browser.
 * 
 * [link](https://core.telegram.org/bots/api#inputfile): https://core.telegram.org/bots/api#inputfile
 * 
 */
@Serializable
object InputFile

