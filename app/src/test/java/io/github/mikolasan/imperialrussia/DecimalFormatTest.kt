package io.github.mikolasan.imperialrussia

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DecimalFormatSymbols
import java.util.Locale


/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DecimalFormatTest {

    val enLocale = Locale("en")
    val ruLocale = Locale("ru")

    @Test
    fun parseDisplayString_zero() {
        assertEquals(0.0, parseDisplayString("0"), 1e-10)
    }

    @Test
    fun parseDisplayString_decimalSymbolEn() {
        assertEquals(1.2, parseDisplayString("1.2", enLocale), 1e-10)
    }

    @Test
    fun parseDisplayString_decimalSymbolRu() {
        assertEquals(1.2, parseDisplayString("1,2", ruLocale), 1e-10)
    }

    @Test
    fun parseDisplayString_groupingSymbolEn() {
        assertEquals(1234.5, parseDisplayString("1,234.5", enLocale), 1e-10)
    }

    @Test
    fun parseDisplayString_groupingSymbolRu() {

        assertEquals(1234.5, parseDisplayString("1"+DecimalFormatSymbols.getInstance(ruLocale).groupingSeparator+"234,5", ruLocale), 1e-10)
    }
}