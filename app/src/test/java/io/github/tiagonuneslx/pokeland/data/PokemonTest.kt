package io.github.tiagonuneslx.pokeland.data

import io.github.tiagonuneslx.pokeland.data.model.Pokemon
import io.github.tiagonuneslx.pokeland.ui.theme.PsychicColor
import org.junit.Test
import org.junit.Assert.*

class PokemonTest {

    @Test
    fun getType() {
        val typeId = "psychic"
        val type = Pokemon.Type.getOrUnknown(typeId)
        assertEquals(type, Pokemon.Type.Psychic)
        assertEquals(type.color, PsychicColor)
        assertEquals("Psychic", Pokemon.Type.Psychic.name)
    }
}