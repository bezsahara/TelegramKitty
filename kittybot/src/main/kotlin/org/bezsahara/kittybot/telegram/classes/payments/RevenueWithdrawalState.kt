package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.json.jsonObject
import org.bezsahara.kittybot.telegram.classes.payments.RevenueWithdrawalStateSucceeded
import org.bezsahara.kittybot.telegram.classes.payments.RevenueWithdrawalStateFailed
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.payments.RevenueWithdrawalState
import org.bezsahara.kittybot.telegram.values.RevenueWithdrawalStateType
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.payments.RevenueWithdrawalStatePending


@Serializable(with = RevenueWithdrawalStateSerializer::class)
sealed interface RevenueWithdrawalState {
    val type: RevenueWithdrawalStateType
}


private object RevenueWithdrawalStateSerializer : JsonContentPolymorphicSerializer<RevenueWithdrawalState>(RevenueWithdrawalState::class) {
    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<RevenueWithdrawalState> {
        return when (element.jsonObject["type"]!!.jsonPrimitive.content) {
            "pending" -> RevenueWithdrawalStatePending.serializer()
            "succeeded" -> RevenueWithdrawalStateSucceeded.serializer()
            "failed" -> RevenueWithdrawalStateFailed.serializer()
            else -> error("Serializer wasn't found for object with key ${element.jsonObject["type"]!!.jsonPrimitive.content}")
        }
    }
}



