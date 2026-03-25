package org.bezsahara.kittybot.bot.json

class SingleList<T>(val el: T) : List<T> {

    override val size: Int
        get() = 1

    override fun contains(element: T): Boolean =
        el == element

    override fun containsAll(elements: Collection<T>): Boolean =
        elements.all { it == el }

    override fun get(index: Int): T =
        if (index == 0) el else throw IndexOutOfBoundsException("Index: $index")

    override fun indexOf(element: T): Int =
        if (el == element) 0 else -1

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<T> =
        listOf(el).iterator()

    override fun lastIndexOf(element: T): Int =
        if (el == element) 0 else -1

    override fun listIterator(): ListIterator<T> =
        listIterator(0)

    override fun listIterator(index: Int): ListIterator<T> {
        require(index in 0..1) { "Index: $index" }
        return object : ListIterator<T> {
            var pos = index
            override fun hasNext(): Boolean = pos == 0
            override fun next(): T {
                if (!hasNext()) throw NoSuchElementException()
                pos++
                return el
            }
            override fun hasPrevious(): Boolean = pos == 1
            override fun previous(): T {
                if (!hasPrevious()) throw NoSuchElementException()
                pos--
                return el
            }
            override fun nextIndex(): Int = if (pos == 0) 1 else 1
            override fun previousIndex(): Int = if (pos == 1) 0 else -1
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<T> =
        when {
            fromIndex == 0 && toIndex == 1 -> this
            fromIndex == 0 && toIndex == 0 -> emptyList()
            fromIndex == 1 && toIndex == 1 -> emptyList()
            else -> throw IndexOutOfBoundsException("fromIndex=$fromIndex, toIndex=$toIndex")
        }
}