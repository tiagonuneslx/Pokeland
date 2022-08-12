package io.github.tiagonuneslx.pokeland.data.model

import androidx.compose.ui.graphics.Color
import io.github.tiagonuneslx.pokeland.ui.theme.*
import io.github.tiagonuneslx.pokeland.util.capitalizeRoot

data class Pokemon(
    val id: String,
    val number: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val types: List<Type>,
    val height: Float, // meters
    val weight: Float, // kg
    val abilitySlots: List<String>,
    val moveSlots: List<String>,
    val baseExperience: Int,
    val stats: Stats,
    val evolutionChainId: Int?,
) {
    enum class Type(
        val color: Color,
    ) {
        Normal(NormalColor),
        Grass(GrassColor),
        Water(WaterColor),
        Fire(FireColor),
        Electric(ElectricColor),
        Fighting(FightingColor),
        Flying(FlyingColor),
        Poison(PoisonColor),
        Ground(GroundColor),
        Rock(RockColor),
        Bug(BugColor),
        Ghost(GhostColor),
        Steel(SteelColor),
        Psychic(PsychicColor),
        Ice(IceColor),
        Dragon(DragonColor),
        Dark(DarkColor),
        Fairy(FairyColor),
        Shadow(ShadowColor),
        Unknown(UnknownColor),
        ;

        companion object {
            fun getOrUnknown(name: String?) = name?.let {
                try {
                    valueOf(name.capitalizeRoot())
                } catch (e: IllegalArgumentException) {
                    Unknown
                }
            } ?: Unknown
        }
    }

    data class Move(
        val id: String,
        val name: String,
        val accuracy: Int?,
        val description: String?,
        val power: Int,
        val pp: Int,
        val type: Type,
    )

    data class Stats(
        val hp: Int,
        val attack: Int,
        val defense: Int,
        val specialAttack: Int,
        val specialDefense: Int,
        val speed: Int,
    )

    data class Evolution(
        val evolvesTo: Pokemon,
        val minLevel: Int?,
    )
}