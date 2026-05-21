package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
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
        val HEART = ReactionEmoji("❤", Known.HEART).register()

        @JvmField
        val THUMBS_UP = ReactionEmoji("👍", Known.THUMBS_UP).register()

        @JvmField
        val THUMBS_DOWN = ReactionEmoji("👎", Known.THUMBS_DOWN).register()

        @JvmField
        val FIRE = ReactionEmoji("🔥", Known.FIRE).register()

        @JvmField
        val SMILING_HEARTS = ReactionEmoji("🥰", Known.SMILING_HEARTS).register()

        @JvmField
        val CLAP = ReactionEmoji("👏", Known.CLAP).register()

        @JvmField
        val GRIN = ReactionEmoji("😁", Known.GRIN).register()

        @JvmField
        val THINKING = ReactionEmoji("🤔", Known.THINKING).register()

        @JvmField
        val MIND_BLOWN = ReactionEmoji("🤯", Known.MIND_BLOWN).register()

        @JvmField
        val SCREAM = ReactionEmoji("😱", Known.SCREAM).register()

        @JvmField
        val CURSING = ReactionEmoji("🤬", Known.CURSING).register()

        @JvmField
        val CRY = ReactionEmoji("😢", Known.CRY).register()

        @JvmField
        val PARTY_POPPER = ReactionEmoji("🎉", Known.PARTY_POPPER).register()

        @JvmField
        val STAR_STRUCK = ReactionEmoji("🤩", Known.STAR_STRUCK).register()

        @JvmField
        val VOMIT = ReactionEmoji("🤮", Known.VOMIT).register()

        @JvmField
        val POO = ReactionEmoji("💩", Known.POO).register()

        @JvmField
        val FOLDED_HANDS = ReactionEmoji("🙏", Known.FOLDED_HANDS).register()

        @JvmField
        val OK_HAND = ReactionEmoji("👌", Known.OK_HAND).register()

        @JvmField
        val DOVE = ReactionEmoji("🕊", Known.DOVE).register()

        @JvmField
        val CLOWN = ReactionEmoji("🤡", Known.CLOWN).register()

        @JvmField
        val YAWN = ReactionEmoji("🥱", Known.YAWN).register()

        @JvmField
        val WOOZY = ReactionEmoji("🥴", Known.WOOZY).register()

        @JvmField
        val HEART_EYES = ReactionEmoji("😍", Known.HEART_EYES).register()

        @JvmField
        val WHALE = ReactionEmoji("🐳", Known.WHALE).register()

        @JvmField
        val HEART_ON_FIRE = ReactionEmoji("❤‍🔥", Known.HEART_ON_FIRE).register()

        @JvmField
        val NEW_MOON_FACE = ReactionEmoji("🌚", Known.NEW_MOON_FACE).register()

        @JvmField
        val HOT_DOG = ReactionEmoji("🌭", Known.HOT_DOG).register()

        @JvmField
        val HUNDRED = ReactionEmoji("💯", Known.HUNDRED).register()

        @JvmField
        val ROFL = ReactionEmoji("🤣", Known.ROFL).register()

        @JvmField
        val LIGHTNING = ReactionEmoji("⚡", Known.LIGHTNING).register()

        @JvmField
        val BANANA = ReactionEmoji("🍌", Known.BANANA).register()

        @JvmField
        val TROPHY = ReactionEmoji("🏆", Known.TROPHY).register()

        @JvmField
        val BROKEN_HEART = ReactionEmoji("💔", Known.BROKEN_HEART).register()

        @JvmField
        val RAISED_EYEBROW = ReactionEmoji("🤨", Known.RAISED_EYEBROW).register()

        @JvmField
        val NEUTRAL_FACE = ReactionEmoji("😐", Known.NEUTRAL_FACE).register()

        @JvmField
        val STRAWBERRY = ReactionEmoji("🍓", Known.STRAWBERRY).register()

        @JvmField
        val CHAMPAGNE = ReactionEmoji("🍾", Known.CHAMPAGNE).register()

        @JvmField
        val KISS_MARK = ReactionEmoji("💋", Known.KISS_MARK).register()

        @JvmField
        val MIDDLE_FINGER = ReactionEmoji("🖕", Known.MIDDLE_FINGER).register()

        @JvmField
        val DEVIL = ReactionEmoji("😈", Known.DEVIL).register()

        @JvmField
        val SLEEPING = ReactionEmoji("😴", Known.SLEEPING).register()

        @JvmField
        val SOB = ReactionEmoji("😭", Known.SOB).register()

        @JvmField
        val NERD = ReactionEmoji("🤓", Known.NERD).register()

        @JvmField
        val GHOST = ReactionEmoji("👻", Known.GHOST).register()

        @JvmField
        val MAN_TECH = ReactionEmoji("👨‍💻", Known.MAN_TECH).register()

        @JvmField
        val EYES = ReactionEmoji("👀", Known.EYES).register()

        @JvmField
        val PUMPKIN = ReactionEmoji("🎃", Known.PUMPKIN).register()

        @JvmField
        val SEE_NO_EVIL = ReactionEmoji("🙈", Known.SEE_NO_EVIL).register()

        @JvmField
        val HALO = ReactionEmoji("😇", Known.HALO).register()

        @JvmField
        val FEAR = ReactionEmoji("😨", Known.FEAR).register()

        @JvmField
        val HANDSHAKE = ReactionEmoji("🤝", Known.HANDSHAKE).register()

        @JvmField
        val WRITING_HAND = ReactionEmoji("✍", Known.WRITING_HAND).register()

        @JvmField
        val HUG = ReactionEmoji("🤗", Known.HUG).register()

        @JvmField
        val SALUTE = ReactionEmoji("🫡", Known.SALUTE).register()

        @JvmField
        val SANTA = ReactionEmoji("🎅", Known.SANTA).register()

        @JvmField
        val CHRISTMAS_TREE = ReactionEmoji("🎄", Known.CHRISTMAS_TREE).register()

        @JvmField
        val SNOWMAN = ReactionEmoji("☃", Known.SNOWMAN).register()

        @JvmField
        val NAIL_POLISH = ReactionEmoji("💅", Known.NAIL_POLISH).register()

        @JvmField
        val ZANY = ReactionEmoji("🤪", Known.ZANY).register()

        @JvmField
        val MOAI = ReactionEmoji("🗿", Known.MOAI).register()

        @JvmField
        val COOL = ReactionEmoji("🆒", Known.COOL).register()

        @JvmField
        val HEART_WITH_ARROW = ReactionEmoji("💘", Known.HEART_WITH_ARROW).register()

        @JvmField
        val HEAR_NO_EVIL = ReactionEmoji("🙉", Known.HEAR_NO_EVIL).register()

        @JvmField
        val UNICORN = ReactionEmoji("🦄", Known.UNICORN).register()

        @JvmField
        val KISS_FACE = ReactionEmoji("😘", Known.KISS_FACE).register()

        @JvmField
        val PILL = ReactionEmoji("💊", Known.PILL).register()

        @JvmField
        val SPEAK_NO_EVIL = ReactionEmoji("🙊", Known.SPEAK_NO_EVIL).register()

        @JvmField
        val SUNGLASSES = ReactionEmoji("😎", Known.SUNGLASSES).register()

        @JvmField
        val ALIEN_MONSTER = ReactionEmoji("👾", Known.ALIEN_MONSTER).register()

        @JvmField
        val MAN_SHRUG = ReactionEmoji("🤷‍♂", Known.MAN_SHRUG).register()

        @JvmField
        val SHRUG = ReactionEmoji("🤷", Known.SHRUG).register()

        @JvmField
        val WOMAN_SHRUG = ReactionEmoji("🤷‍♀", Known.WOMAN_SHRUG).register()

        @JvmField
        val ANGRY = ReactionEmoji("😡", Known.ANGRY).register()

        override fun create(value: String): ReactionEmoji {
            return ReactionEmoji(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        HEART,
        THUMBS_UP,
        THUMBS_DOWN,
        FIRE,
        SMILING_HEARTS,
        CLAP,
        GRIN,
        THINKING,
        MIND_BLOWN,
        SCREAM,
        CURSING,
        CRY,
        PARTY_POPPER,
        STAR_STRUCK,
        VOMIT,
        POO,
        FOLDED_HANDS,
        OK_HAND,
        DOVE,
        CLOWN,
        YAWN,
        WOOZY,
        HEART_EYES,
        WHALE,
        HEART_ON_FIRE,
        NEW_MOON_FACE,
        HOT_DOG,
        HUNDRED,
        ROFL,
        LIGHTNING,
        BANANA,
        TROPHY,
        BROKEN_HEART,
        RAISED_EYEBROW,
        NEUTRAL_FACE,
        STRAWBERRY,
        CHAMPAGNE,
        KISS_MARK,
        MIDDLE_FINGER,
        DEVIL,
        SLEEPING,
        SOB,
        NERD,
        GHOST,
        MAN_TECH,
        EYES,
        PUMPKIN,
        SEE_NO_EVIL,
        HALO,
        FEAR,
        HANDSHAKE,
        WRITING_HAND,
        HUG,
        SALUTE,
        SANTA,
        CHRISTMAS_TREE,
        SNOWMAN,
        NAIL_POLISH,
        ZANY,
        MOAI,
        COOL,
        HEART_WITH_ARROW,
        HEAR_NO_EVIL,
        UNICORN,
        KISS_FACE,
        PILL,
        SPEAK_NO_EVIL,
        SUNGLASSES,
        ALIEN_MONSTER,
        MAN_SHRUG,
        SHRUG,
        WOMAN_SHRUG,
        ANGRY;

        fun toReactionEmoji(): ReactionEmoji {
            return ReactionEmoji.mapGet(this)
        }
    }

    override fun toString(): String {
        return "ReactionEmoji($value)"
    }
}

internal object ReactionEmojiSerializer : EnumLikeJsonSerializer<ReactionEmoji>("ReactionEmoji", ReactionEmoji)

