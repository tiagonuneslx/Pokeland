package io.github.tiagonuneslx.pokeland.util

import org.junit.Test
import org.junit.Assert.*

class UtilTest {

    @Test
    fun capitalizeRoot() {
        val type = "psychic"
        assertEquals("Psychic", type.capitalizeRoot())
    }
}