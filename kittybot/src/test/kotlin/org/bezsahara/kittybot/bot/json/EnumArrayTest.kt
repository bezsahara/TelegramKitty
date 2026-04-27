package org.bezsahara.kittybot.bot.json

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EnumArrayTest {
    private enum class Letters {
        q,w,e,r,t,y,u,i,o,p,a,s,d,f,g,h,j,k,l,z,x,c,v,b,n,m
    }

    @Test
    fun enumTest() {
        val array = EnumArray<Letters, String>(Letters::class.java)

        val constants = Letters.entries

        constants.forEach {
            array[it] = it.name
        }

        val list = array.toList()

        list.forEachIndexed { index, string ->
            assertNotNull(string)
            assertEquals(string, constants[index].name)
        }
    }
}
