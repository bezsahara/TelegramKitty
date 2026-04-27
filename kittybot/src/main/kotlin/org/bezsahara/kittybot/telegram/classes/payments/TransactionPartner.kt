package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerAffiliateProgram
import kotlinx.serialization.json.jsonObject
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerFragment
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import kotlinx.serialization.DeserializationStrategy
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerTelegramAds
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerUser
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerChat
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartner
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerTelegramApi
import org.bezsahara.kittybot.telegram.classes.payments.TransactionPartnerOther
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable


@Serializable(with = TransactionPartnerSerializer::class)
sealed interface TransactionPartner {
    val type: String
}


private object TransactionPartnerSerializer : JsonContentPolymorphicSerializer<TransactionPartner>(TransactionPartner::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<TransactionPartner> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "user" -> TransactionPartnerUser.serializer()
            "chat" -> TransactionPartnerChat.serializer()
            "affiliate_program" -> TransactionPartnerAffiliateProgram.serializer()
            "fragment" -> TransactionPartnerFragment.serializer()
            "telegram_ads" -> TransactionPartnerTelegramAds.serializer()
            "telegram_api" -> TransactionPartnerTelegramApi.serializer()
            "other" -> TransactionPartnerOther.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



