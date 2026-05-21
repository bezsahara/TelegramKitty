package org.bezsahara.kittybot.telegram.classes.core.update

import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import sun.misc.Unsafe
import sun.reflect.ReflectionFactory
import java.lang.reflect.Method
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdateAntics {
    private fun getUnsafe(): Unsafe {
        val fu = Unsafe::class.java.getDeclaredField("theUnsafe")
        fu.isAccessible = true
        val unsafe = fu.get(null) as Unsafe
        return unsafe
    }

    @Test
    fun `update ordinals are correct and unique`() {

        val ordinalSet = hashSetOf<Int>()
        telegramUpdateKinds.forEach { updateKind ->
            if (ordinalSet.contains(updateKind.ordinal)) {
                error("${updateKind.ordinal} is not correct")
            }
            ordinalSet.add(updateKind.ordinal)
        }

        assertEquals(ordinalSet.size, telegramUpdateKinds.size)

        fun isContinuousFromZero(nums: List<Int>): Boolean {
            if (nums[0] != 0) return false

            for (i in 1 until nums.size) {
                if (nums[i] != nums[i - 1] + 1) return false
            }
            return true
        }

        assertTrue(isContinuousFromZero(ordinalSet.toList().sorted()))

        val unsafe = getUnsafe()

        fun lookupByClass(cls: Class<*>): UpdKind {
            return telegramUpdateKinds.first { it.clazz == cls }
        }

        val updKindClass = UpdKind::class.java

        val subClasses = updKindClass.permittedSubclasses

        assertEquals(telegramUpdateKinds.size, subClasses.size)

        val initedClasses = subClasses.map {
            val actualClass = it.enclosingClass!!
            if (actualClass == SyntheticUpdate::class.java) {
                FinalSynthetic()
            } else {
            unsafe.allocateInstance(actualClass) as Update} to lookupByClass(actualClass)
        }

        val ordinalMethod: Method = Update::class.java.declaredMethods.first { it.name == "getOrdinal" }

        initedClasses.forEach { (upd, updKind) ->
            val expected = ordinalMethod.invoke(upd)!! as Int
            assertEquals(expected, updKind.ordinal)
        }

    }

    class FinalSynthetic : SyntheticUpdate() {
        override val updateId: Long
            get() = TODO("Not yet implemented")

        override fun chatIdOrNull(): ChatId? {
            TODO("Not yet implemented")
        }

        override fun userIdOrNull(): ChatId? {
            TODO("Not yet implemented")
        }
    }
}