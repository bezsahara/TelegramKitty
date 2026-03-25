package org.bezsahara.kittybot.telegram.classes.business

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(CurrencyKindSerializer::class)
@JvmInline
value class CurrencyKind(val value: String) {
    companion object {
        /** United Arab Emirates Dirham */ val AED = CurrencyKind("AED")
        /** Afghan Afghani */ val AFN = CurrencyKind("AFN")
        /** Albanian Lek */ val ALL = CurrencyKind("ALL")
        /** Armenian Dram */ val AMD = CurrencyKind("AMD")
        /** Argentine Peso */ val ARS = CurrencyKind("ARS")
        /** Australian Dollar */ val AUD = CurrencyKind("AUD")
        /** Azerbaijani Manat */ val AZN = CurrencyKind("AZN")
        /** Bosnia & Herzegovina Convertible Mark */ val BAM = CurrencyKind("BAM")
        /** Bangladeshi Taka */ val BDT = CurrencyKind("BDT")
        /** Bulgarian Lev */ val BGN = CurrencyKind("BGN")
        /** Bahraini Dinar */ val BHD = CurrencyKind("BHD")
        /** Brunei Dollar */ val BND = CurrencyKind("BND")
        /** Bolivian Boliviano */ val BOB = CurrencyKind("BOB")
        /** Brazilian Real */ val BRL = CurrencyKind("BRL")
        /** Belarusian Ruble */ val BYN = CurrencyKind("BYN")
        /** Canadian Dollar */ val CAD = CurrencyKind("CAD")
        /** Swiss Franc */ val CHF = CurrencyKind("CHF")
        /** Chilean Peso */ val CLP = CurrencyKind("CLP")
        /** Chinese Renminbi Yuan */ val CNY = CurrencyKind("CNY")
        /** Colombian Peso */ val COP = CurrencyKind("COP")
        /** Costa Rican Colón */ val CRC = CurrencyKind("CRC")
        /** Czech Koruna */ val CZK = CurrencyKind("CZK")
        /** Danish Krone */ val DKK = CurrencyKind("DKK")
        /** Dominican Peso */ val DOP = CurrencyKind("DOP")
        /** Algerian Dinar */ val DZD = CurrencyKind("DZD")
        /** Egyptian Pound */ val EGP = CurrencyKind("EGP")
        /** Ethiopian Birr */ val ETB = CurrencyKind("ETB")
        /** Euro */ val EUR = CurrencyKind("EUR")
        /** British Pound Sterling */ val GBP = CurrencyKind("GBP")
        /** Georgian Lari */ val GEL = CurrencyKind("GEL")
        /** Ghanaian Cedi */ val GHS = CurrencyKind("GHS")
        /** Guatemalan Quetzal */ val GTQ = CurrencyKind("GTQ")
        /** Hong Kong Dollar */ val HKD = CurrencyKind("HKD")
        /** Honduran Lempira */ val HNL = CurrencyKind("HNL")
        /** Croatian Kuna */ val HRK = CurrencyKind("HRK")
        /** Hungarian Forint */ val HUF = CurrencyKind("HUF")
        /** Indonesian Rupiah */ val IDR = CurrencyKind("IDR")
        /** Israeli New Sheqel */ val ILS = CurrencyKind("ILS")
        /** Indian Rupee */ val INR = CurrencyKind("INR")
        /** Iraqi Dinar */ val IQD = CurrencyKind("IQD")
        /** Iranian Rial */ val IRR = CurrencyKind("IRR")
        /** Icelandic Króna */ val ISK = CurrencyKind("ISK")
        /** Jamaican Dollar */ val JMD = CurrencyKind("JMD")
        /** Jordanian Dinar */ val JOD = CurrencyKind("JOD")
        /** Japanese Yen */ val JPY = CurrencyKind("JPY")
        /** Kenyan Shilling */ val KES = CurrencyKind("KES")
        /** Kyrgyzstani Som */ val KGS = CurrencyKind("KGS")
        /** South Korean Won */ val KRW = CurrencyKind("KRW")
        /** Kazakhstani Tenge */ val KZT = CurrencyKind("KZT")
        /** Lebanese Pound */ val LBP = CurrencyKind("LBP")
        /** Sri Lankan Rupee */ val LKR = CurrencyKind("LKR")
        /** Moroccan Dirham */ val MAD = CurrencyKind("MAD")
        /** Moldovan Leu */ val MDL = CurrencyKind("MDL")
        /** Myanmar Kyat */ val MMK = CurrencyKind("MMK")
        /** Mongolian Tögrög */ val MNT = CurrencyKind("MNT")
        /** Macanese Pataca */ val MOP = CurrencyKind("MOP")
        /** Mauritian Rupee */ val MUR = CurrencyKind("MUR")
        /** Maldivian Rufiyaa */ val MVR = CurrencyKind("MVR")
        /** Mexican Peso */ val MXN = CurrencyKind("MXN")
        /** Malaysian Ringgit */ val MYR = CurrencyKind("MYR")
        /** Mozambican Metical */ val MZN = CurrencyKind("MZN")
        /** Nigerian Naira */ val NGN = CurrencyKind("NGN")
        /** Nicaraguan Córdoba */ val NIO = CurrencyKind("NIO")
        /** Norwegian Krone */ val NOK = CurrencyKind("NOK")
        /** Nepalese Rupee */ val NPR = CurrencyKind("NPR")
        /** New Zealand Dollar */ val NZD = CurrencyKind("NZD")
        /** Panamanian Balboa */ val PAB = CurrencyKind("PAB")
        /** Peruvian Nuevo Sol */ val PEN = CurrencyKind("PEN")
        /** Philippine Peso */ val PHP = CurrencyKind("PHP")
        /** Pakistani Rupee */ val PKR = CurrencyKind("PKR")
        /** Polish Złoty */ val PLN = CurrencyKind("PLN")
        /** Paraguayan Guaraní */ val PYG = CurrencyKind("PYG")
        /** Qatari Riyal */ val QAR = CurrencyKind("QAR")
        /** Romanian Leu */ val RON = CurrencyKind("RON")
        /** Serbian Dinar */ val RSD = CurrencyKind("RSD")
        /** Russian Ruble */ val RUB = CurrencyKind("RUB")
        /** Saudi Riyal */ val SAR = CurrencyKind("SAR")
        /** Swedish Krona */ val SEK = CurrencyKind("SEK")
        /** Singapore Dollar */ val SGD = CurrencyKind("SGD")
        /** Syrian Pound */ val SYP = CurrencyKind("SYP")
        /** Thai Baht */ val THB = CurrencyKind("THB")
        /** Tajikistani Somoni */ val TJS = CurrencyKind("TJS")
        /** Turkish Lira */ val TRY = CurrencyKind("TRY")
        /** Trinidad and Tobago Dollar */ val TTD = CurrencyKind("TTD")
        /** New Taiwan Dollar */ val TWD = CurrencyKind("TWD")
        /** Tanzanian Shilling */ val TZS = CurrencyKind("TZS")
        /** Ukrainian Hryvnia */ val UAH = CurrencyKind("UAH")
        /** Ugandan Shilling */ val UGX = CurrencyKind("UGX")
        /** United States Dollar */ val USD = CurrencyKind("USD")
        /** Uruguayan Peso */ val UYU = CurrencyKind("UYU")
        /** Uzbekistani Som */ val UZS = CurrencyKind("UZS")
        /** Vietnamese Đồng */ val VND = CurrencyKind("VND")
        /** Yemeni Rial */ val YER = CurrencyKind("YER")
        /** South African Rand */ val ZAR = CurrencyKind("ZAR")

        val values: List<CurrencyKind> = listOf(
            AED, AFN, ALL, AMD, ARS, AUD, AZN, BAM, BDT, BGN, BHD, BND, BOB, BRL, BYN, CAD,
            CHF, CLP, CNY, COP, CRC, CZK, DKK, DOP, DZD, EGP, ETB, EUR, GBP, GEL, GHS, GTQ,
            HKD, HNL, HRK, HUF, IDR, ILS, INR, IQD, IRR, ISK, JMD, JOD, JPY, KES, KGS, KRW,
            KZT, LBP, LKR, MAD, MDL, MMK, MNT, MOP, MUR, MVR, MXN, MYR, MZN, NGN, NIO, NOK,
            NPR, NZD, PAB, PEN, PHP, PKR, PLN, PYG, QAR, RON, RSD, RUB, SAR, SEK, SGD, SYP,
            THB, TJS, TRY, TTD, TWD, TZS, UAH, UGX, USD, UYU, UZS, VND, YER, ZAR
        )
    }
}


object CurrencyKindSerializer : KSerializer<CurrencyKind> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CurrencyKind", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: CurrencyKind) =
        encoder.encodeString(value.value)

    override fun deserialize(decoder: Decoder): CurrencyKind =
        CurrencyKind(decoder.decodeString())
}