package org.bezsahara.kittybot.telegram.classes.replies


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat
import org.bezsahara.kittybot.telegram.classes.contacts.Contact
import org.bezsahara.kittybot.telegram.classes.games.Dice
import org.bezsahara.kittybot.telegram.classes.games.Game
import org.bezsahara.kittybot.telegram.classes.giveaways.Giveaway
import org.bezsahara.kittybot.telegram.classes.giveaways.GiveawayWinners
import org.bezsahara.kittybot.telegram.classes.locations.Location
import org.bezsahara.kittybot.telegram.classes.locations.Venue
import org.bezsahara.kittybot.telegram.classes.media.*
import org.bezsahara.kittybot.telegram.classes.media.photos.PhotoSize
import org.bezsahara.kittybot.telegram.classes.messages.origins.MessageOrigin
import org.bezsahara.kittybot.telegram.classes.payments.Invoice
import org.bezsahara.kittybot.telegram.classes.polls.Poll
import org.bezsahara.kittybot.telegram.classes.previews.LinkPreviewOptions
import org.bezsahara.kittybot.telegram.classes.stickers.Sticker


/**
 * This object contains information about a message that is being replied to, which may come from another chat or forum topic.
 * 
 * *[link](https://core.telegram.org/bots/api#externalreplyinfo)*: https://core.telegram.org/bots/api#externalreplyinfo
 * 
 * @param origin Origin of the message replied to by the given message
 * @param chat Optional. Chat the original message belongs to. Available only if the chat is a supergroup or a channel.
 * @param messageId Optional. Unique message identifier inside the original chat. Available only if the original chat is a supergroup or a channel.
 * @param linkPreviewOptions Optional. Options used for link preview generation for the original message, if it is a text message
 * @param animation Optional. Message is an animation, information about the animation
 * @param audio Optional. Message is an audio file, information about the file
 * @param document Optional. Message is a general file, information about the file
 * @param photo Optional. Message is a photo, available sizes of the photo
 * @param sticker Optional. Message is a sticker, information about the sticker
 * @param story Optional. Message is a forwarded story
 * @param video Optional. Message is a video, information about the video
 * @param videoNote Optional. Message is a video note, information about the video message
 * @param voice Optional. Message is a voice message, information about the file
 * @param hasMediaSpoiler Optional. True, if the message media is covered by a spoiler animation
 * @param contact Optional. Message is a shared contact, information about the contact
 * @param dice Optional. Message is a dice with random value
 * @param game Optional. Message is a game, information about the game. More about games: https://core.telegram.org/bots/api#games
 * @param giveaway Optional. Message is a scheduled giveaway, information about the giveaway
 * @param giveawayWinners Optional. A giveaway with public winners was completed
 * @param invoice Optional. Message is an invoice for a payment, information about the invoice. More about payments: https://core.telegram.org/bots/api#payments
 * @param location Optional. Message is a shared location, information about the location
 * @param poll Optional. Message is a native poll, information about the poll
 * @param venue Optional. Message is a venue, information about the venue
 */
@Serializable
data class ExternalReplyInfo(
    val origin: MessageOrigin,
    val chat: Chat? = null,
    @SerialName("message_id") val messageId: Long? = null,
    @SerialName("link_preview_options") val linkPreviewOptions: LinkPreviewOptions? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val story: Story? = null,
    val video: Video? = null,
    @SerialName("video_note") val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    @SerialName("has_media_spoiler") val hasMediaSpoiler: Boolean? = null,
    val contact: Contact? = null,
    val dice: Dice? = null,
    val game: Game? = null,
    val giveaway: Giveaway? = null,
    @SerialName("giveaway_winners") val giveawayWinners: GiveawayWinners? = null,
    val invoice: Invoice? = null,
    val location: Location? = null,
    val poll: Poll? = null,
    val venue: Venue? = null
)

