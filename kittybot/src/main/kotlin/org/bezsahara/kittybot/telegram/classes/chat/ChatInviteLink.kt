package org.bezsahara.kittybot.telegram.classes.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * Represents an invite link for a chat.
 * 
 * [link](https://core.telegram.org/bots/api#chatinvitelink): https://core.telegram.org/bots/api#chatinvitelink
 * 
 * @param inviteLink The invite link. If the link was created by another chat administrator, then the second part of the link will be replaced with "...".
 * @param creator Creator of the link
 * @param createsJoinRequest True, if users joining the chat via the link need to be approved by chat administrators
 * @param isPrimary True, if the link is primary
 * @param isRevoked True, if the link is revoked
 * @param name Optional. Invite link name
 * @param expireDate Optional. Point in time (Unix timestamp) when the link will expire or has been expired
 * @param memberLimit Optional. The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @param pendingJoinRequestCount Optional. Number of pending join requests created using this link
 * @param subscriptionPeriod Optional. The number of seconds the subscription will be active for before the next payment
 * @param subscriptionPrice Optional. The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a member of the chat using the link
 */
@Serializable
data class ChatInviteLink(
    @SerialName("invite_link") val inviteLink: String,
    val creator: User,
    @SerialName("creates_join_request") val createsJoinRequest: Boolean,
    @SerialName("is_primary") val isPrimary: Boolean,
    @SerialName("is_revoked") val isRevoked: Boolean,
    val name: String? = null,
    @SerialName("expire_date") val expireDate: Long? = null,
    @SerialName("member_limit") val memberLimit: Long? = null,
    @SerialName("pending_join_request_count") val pendingJoinRequestCount: Long? = null,
    @SerialName("subscription_period") val subscriptionPeriod: Long? = null,
    @SerialName("subscription_price") val subscriptionPrice: Long? = null
)

