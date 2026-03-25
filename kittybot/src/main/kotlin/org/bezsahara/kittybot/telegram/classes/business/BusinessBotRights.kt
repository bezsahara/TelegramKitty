package org.bezsahara.kittybot.telegram.classes.business

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents the rights of a business bot.
 * 
 * [link](https://core.telegram.org/bots/api#businessbotrights): https://core.telegram.org/bots/api#businessbotrights
 * 
 * @param canReply Optional. True, if the bot can send and edit messages in the private chats that had incoming messages in the last 24 hours
 * @param canReadMessages Optional. True, if the bot can mark incoming private messages as read
 * @param canDeleteSentMessages Optional. True, if the bot can delete messages sent by the bot
 * @param canDeleteAllMessages Optional. True, if the bot can delete all private messages in managed chats
 * @param canEditName Optional. True, if the bot can edit the first and last name of the business account
 * @param canEditBio Optional. True, if the bot can edit the bio of the business account
 * @param canEditProfilePhoto Optional. True, if the bot can edit the profile photo of the business account
 * @param canEditUsername Optional. True, if the bot can edit the username of the business account
 * @param canChangeGiftSettings Optional. True, if the bot can change the privacy settings pertaining to gifts for the business account
 * @param canViewGiftsAndStars Optional. True, if the bot can view gifts and the amount of Telegram Stars owned by the business account
 * @param canConvertGiftsToStars Optional. True, if the bot can convert regular gifts owned by the business account to Telegram Stars
 * @param canTransferAndUpgradeGifts Optional. True, if the bot can transfer and upgrade gifts owned by the business account
 * @param canTransferStars Optional. True, if the bot can transfer Telegram Stars received by the business account to its own account, or use them to upgrade and transfer gifts
 * @param canManageStories Optional. True, if the bot can post, edit and delete stories on behalf of the business account
 */
@Serializable
data class BusinessBotRights(
    @SerialName("can_reply") val canReply: Boolean? = null,
    @SerialName("can_read_messages") val canReadMessages: Boolean? = null,
    @SerialName("can_delete_sent_messages") val canDeleteSentMessages: Boolean? = null,
    @SerialName("can_delete_all_messages") val canDeleteAllMessages: Boolean? = null,
    @SerialName("can_edit_name") val canEditName: Boolean? = null,
    @SerialName("can_edit_bio") val canEditBio: Boolean? = null,
    @SerialName("can_edit_profile_photo") val canEditProfilePhoto: Boolean? = null,
    @SerialName("can_edit_username") val canEditUsername: Boolean? = null,
    @SerialName("can_change_gift_settings") val canChangeGiftSettings: Boolean? = null,
    @SerialName("can_view_gifts_and_stars") val canViewGiftsAndStars: Boolean? = null,
    @SerialName("can_convert_gifts_to_stars") val canConvertGiftsToStars: Boolean? = null,
    @SerialName("can_transfer_and_upgrade_gifts") val canTransferAndUpgradeGifts: Boolean? = null,
    @SerialName("can_transfer_stars") val canTransferStars: Boolean? = null,
    @SerialName("can_manage_stories") val canManageStories: Boolean? = null
)

