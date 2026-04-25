package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type ReactionTypeEmoji.emoji
 */
@Serializable(with = ReactionEmojiSerializer::class)
class ReactionEmoji internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<ReactionEmoji.Known>() {
    companion object : ResolveEnumLikeBig<ReactionEmoji, Known>(Known::class.java) {
        @JvmField
        val U_2764 = ReactionEmoji("❤", Known.U_2764).register()

        @JvmField
        val U_1F44D = ReactionEmoji("👍", Known.U_1F44D).register()

        @JvmField
        val U_1F44E = ReactionEmoji("👎", Known.U_1F44E).register()

        @JvmField
        val U_1F525 = ReactionEmoji("🔥", Known.U_1F525).register()

        @JvmField
        val U_1F970 = ReactionEmoji("🥰", Known.U_1F970).register()

        @JvmField
        val U_1F44F = ReactionEmoji("👏", Known.U_1F44F).register()

        @JvmField
        val U_1F601 = ReactionEmoji("😁", Known.U_1F601).register()

        @JvmField
        val U_1F914 = ReactionEmoji("🤔", Known.U_1F914).register()

        @JvmField
        val U_1F92F = ReactionEmoji("🤯", Known.U_1F92F).register()

        @JvmField
        val U_1F631 = ReactionEmoji("😱", Known.U_1F631).register()

        @JvmField
        val U_1F92C = ReactionEmoji("🤬", Known.U_1F92C).register()

        @JvmField
        val U_1F622 = ReactionEmoji("😢", Known.U_1F622).register()

        @JvmField
        val U_1F389 = ReactionEmoji("🎉", Known.U_1F389).register()

        @JvmField
        val U_1F929 = ReactionEmoji("🤩", Known.U_1F929).register()

        @JvmField
        val U_1F92E = ReactionEmoji("🤮", Known.U_1F92E).register()

        @JvmField
        val U_1F4A9 = ReactionEmoji("💩", Known.U_1F4A9).register()

        @JvmField
        val U_1F64F = ReactionEmoji("🙏", Known.U_1F64F).register()

        @JvmField
        val U_1F44C = ReactionEmoji("👌", Known.U_1F44C).register()

        @JvmField
        val U_1F54A = ReactionEmoji("🕊", Known.U_1F54A).register()

        @JvmField
        val U_1F921 = ReactionEmoji("🤡", Known.U_1F921).register()

        @JvmField
        val U_1F971 = ReactionEmoji("🥱", Known.U_1F971).register()

        @JvmField
        val U_1F974 = ReactionEmoji("🥴", Known.U_1F974).register()

        @JvmField
        val U_1F60D = ReactionEmoji("😍", Known.U_1F60D).register()

        @JvmField
        val U_1F433 = ReactionEmoji("🐳", Known.U_1F433).register()

        @JvmField
        val U_2764_200D_1F525 = ReactionEmoji("❤‍🔥", Known.U_2764_200D_1F525).register()

        @JvmField
        val U_1F31A = ReactionEmoji("🌚", Known.U_1F31A).register()

        @JvmField
        val U_1F32D = ReactionEmoji("🌭", Known.U_1F32D).register()

        @JvmField
        val U_1F4AF = ReactionEmoji("💯", Known.U_1F4AF).register()

        @JvmField
        val U_1F923 = ReactionEmoji("🤣", Known.U_1F923).register()

        @JvmField
        val U_26A1 = ReactionEmoji("⚡", Known.U_26A1).register()

        @JvmField
        val U_1F34C = ReactionEmoji("🍌", Known.U_1F34C).register()

        @JvmField
        val U_1F3C6 = ReactionEmoji("🏆", Known.U_1F3C6).register()

        @JvmField
        val U_1F494 = ReactionEmoji("💔", Known.U_1F494).register()

        @JvmField
        val U_1F928 = ReactionEmoji("🤨", Known.U_1F928).register()

        @JvmField
        val U_1F610 = ReactionEmoji("😐", Known.U_1F610).register()

        @JvmField
        val U_1F353 = ReactionEmoji("🍓", Known.U_1F353).register()

        @JvmField
        val U_1F37E = ReactionEmoji("🍾", Known.U_1F37E).register()

        @JvmField
        val U_1F48B = ReactionEmoji("💋", Known.U_1F48B).register()

        @JvmField
        val U_1F595 = ReactionEmoji("🖕", Known.U_1F595).register()

        @JvmField
        val U_1F608 = ReactionEmoji("😈", Known.U_1F608).register()

        @JvmField
        val U_1F634 = ReactionEmoji("😴", Known.U_1F634).register()

        @JvmField
        val U_1F62D = ReactionEmoji("😭", Known.U_1F62D).register()

        @JvmField
        val U_1F913 = ReactionEmoji("🤓", Known.U_1F913).register()

        @JvmField
        val U_1F47B = ReactionEmoji("👻", Known.U_1F47B).register()

        @JvmField
        val U_1F468_200D_1F4BB = ReactionEmoji("👨‍💻", Known.U_1F468_200D_1F4BB).register()

        @JvmField
        val U_1F440 = ReactionEmoji("👀", Known.U_1F440).register()

        @JvmField
        val U_1F383 = ReactionEmoji("🎃", Known.U_1F383).register()

        @JvmField
        val U_1F648 = ReactionEmoji("🙈", Known.U_1F648).register()

        @JvmField
        val U_1F607 = ReactionEmoji("😇", Known.U_1F607).register()

        @JvmField
        val U_1F628 = ReactionEmoji("😨", Known.U_1F628).register()

        @JvmField
        val U_1F91D = ReactionEmoji("🤝", Known.U_1F91D).register()

        @JvmField
        val U_270D = ReactionEmoji("✍", Known.U_270D).register()

        @JvmField
        val U_1F917 = ReactionEmoji("🤗", Known.U_1F917).register()

        @JvmField
        val U_1FAE1 = ReactionEmoji("🫡", Known.U_1FAE1).register()

        @JvmField
        val U_1F385 = ReactionEmoji("🎅", Known.U_1F385).register()

        @JvmField
        val U_1F384 = ReactionEmoji("🎄", Known.U_1F384).register()

        @JvmField
        val U_2603 = ReactionEmoji("☃", Known.U_2603).register()

        @JvmField
        val U_1F485 = ReactionEmoji("💅", Known.U_1F485).register()

        @JvmField
        val U_1F92A = ReactionEmoji("🤪", Known.U_1F92A).register()

        @JvmField
        val U_1F5FF = ReactionEmoji("🗿", Known.U_1F5FF).register()

        @JvmField
        val U_1F192 = ReactionEmoji("🆒", Known.U_1F192).register()

        @JvmField
        val U_1F498 = ReactionEmoji("💘", Known.U_1F498).register()

        @JvmField
        val U_1F649 = ReactionEmoji("🙉", Known.U_1F649).register()

        @JvmField
        val U_1F984 = ReactionEmoji("🦄", Known.U_1F984).register()

        @JvmField
        val U_1F618 = ReactionEmoji("😘", Known.U_1F618).register()

        @JvmField
        val U_1F48A = ReactionEmoji("💊", Known.U_1F48A).register()

        @JvmField
        val U_1F64A = ReactionEmoji("🙊", Known.U_1F64A).register()

        @JvmField
        val U_1F60E = ReactionEmoji("😎", Known.U_1F60E).register()

        @JvmField
        val U_1F47E = ReactionEmoji("👾", Known.U_1F47E).register()

        @JvmField
        val U_1F937_200D_2642 = ReactionEmoji("🤷‍♂", Known.U_1F937_200D_2642).register()

        @JvmField
        val U_1F937 = ReactionEmoji("🤷", Known.U_1F937).register()

        @JvmField
        val U_1F937_200D_2640 = ReactionEmoji("🤷‍♀", Known.U_1F937_200D_2640).register()

        @JvmField
        val U_1F621 = ReactionEmoji("😡", Known.U_1F621).register()

        override fun create(value: String): ReactionEmoji {
            return ReactionEmoji(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        U_2764,
        U_1F44D,
        U_1F44E,
        U_1F525,
        U_1F970,
        U_1F44F,
        U_1F601,
        U_1F914,
        U_1F92F,
        U_1F631,
        U_1F92C,
        U_1F622,
        U_1F389,
        U_1F929,
        U_1F92E,
        U_1F4A9,
        U_1F64F,
        U_1F44C,
        U_1F54A,
        U_1F921,
        U_1F971,
        U_1F974,
        U_1F60D,
        U_1F433,
        U_2764_200D_1F525,
        U_1F31A,
        U_1F32D,
        U_1F4AF,
        U_1F923,
        U_26A1,
        U_1F34C,
        U_1F3C6,
        U_1F494,
        U_1F928,
        U_1F610,
        U_1F353,
        U_1F37E,
        U_1F48B,
        U_1F595,
        U_1F608,
        U_1F634,
        U_1F62D,
        U_1F913,
        U_1F47B,
        U_1F468_200D_1F4BB,
        U_1F440,
        U_1F383,
        U_1F648,
        U_1F607,
        U_1F628,
        U_1F91D,
        U_270D,
        U_1F917,
        U_1FAE1,
        U_1F385,
        U_1F384,
        U_2603,
        U_1F485,
        U_1F92A,
        U_1F5FF,
        U_1F192,
        U_1F498,
        U_1F649,
        U_1F984,
        U_1F618,
        U_1F48A,
        U_1F64A,
        U_1F60E,
        U_1F47E,
        U_1F937_200D_2642,
        U_1F937,
        U_1F937_200D_2640,
        U_1F621;

        fun toReactionEmoji(): ReactionEmoji {
            return ReactionEmoji.mapGet(this)
        }
    }

    override fun toString(): String {
        return "ReactionEmoji($value)"
    }
}

internal object ReactionEmojiSerializer : EnumLikeJsonSerializer<ReactionEmoji>("ReactionEmoji", ReactionEmoji)

