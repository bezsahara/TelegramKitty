package org.bezsahara.kittybot.telegram.classes.core.update

/**
 * Metadata about types of Update. It is always and only applied to
 * companion objects of Update subclasses. So to specify update kind
 * you just need to write, for example, `MessageUpdate` Without call to constructor!
 * @see MessageUpdate
 * @see CallbackQueryUpdate
 * @see PreCheckoutQueryUpdate
 */
sealed class UpdateKind<T>(
    @JvmField val ordinal: Int,
    @JvmField val clazz: Class<T>,
    @JvmField val name: String
)
