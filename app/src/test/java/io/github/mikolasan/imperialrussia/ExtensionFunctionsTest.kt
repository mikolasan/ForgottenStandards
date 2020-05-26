package io.github.mikolasan.imperialrussia

import org.junit.Test

class ExtensionFunctionsTest {
    @Test
    fun moveToFrontFrom_usesCorrectIndices() {
        val a = arrayOf(0, 1, 2, 3, 4)
        a.moveToFrontFrom(3)
//        println(a.joinToString())
        assert(a contentEquals arrayOf(3, 0, 1, 2, 4))
    }

    @Test
    fun moveToFront_usesCorrectIndices() {
        val a = arrayOf(0, 1, 2, 3, 4)
        a.moveToFront(3)
//        println(a.joinToString())
        assert(a contentEquals arrayOf(3, 0, 1, 2, 4))
    }
}