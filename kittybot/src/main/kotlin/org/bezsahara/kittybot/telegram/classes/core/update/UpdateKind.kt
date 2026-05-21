package org.bezsahara.kittybot.telegram.classes.core.update


/**
 * Metadata about types of Update. It is always and only applied to
 * companion objects of Update subclasses. So to specify update kind
 * you just need to write, for example, `MessageUpdate` Without call to constructor!
 * @see MessageUpdate
 * @see CallbackQueryUpdate
 * @see PreCheckoutQueryUpdate
 */
sealed class UpdKind(
    @JvmField val ordinal: Int,
    @JvmField val clazz: Class<out Update>,
    @JvmField val name: String
) {
    fun toSet(): Set<UpdKind> = setOf(this)

    override fun toString(): String {
        return "UpdateKind($name)"
    }
}

@Deprecated("Use UpdKind instead without type param", ReplaceWith("UpdKind"))
typealias UpdateKind<T> = UpdKind

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
@Deprecated("Use UpdKind.toSet() instead", ReplaceWith("this.toSet()"))
fun UpdKind.toSet(): Set<UpdKind> = setOf(this)
