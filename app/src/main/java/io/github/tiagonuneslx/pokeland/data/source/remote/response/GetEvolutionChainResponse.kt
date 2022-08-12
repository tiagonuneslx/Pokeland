package io.github.tiagonuneslx.pokeland.data.source.remote.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GetEvolutionChainResponse(
    @SerializedName("baby_trigger_item")
    val babyTriggerItem: Any?,
    @SerializedName("chain")
    val chain: Chain,
    @SerializedName("id")
    val id: Int
) {
    @Keep
    data class Chain(
        @SerializedName("evolution_details")
        val evolutionDetails: List<EvolutionDetail>,
        @SerializedName("evolves_to")
        val evolvesTo: List<Chain>,
        @SerializedName("is_baby")
        val isBaby: Boolean,
        @SerializedName("species")
        val species: Species,
    ) {
        @Keep
        data class EvolutionDetail(
            @SerializedName("gender")
            val gender: Any?,
            @SerializedName("held_item")
            val heldItem: Any?,
            @SerializedName("item")
            val item: Any?,
            @SerializedName("known_move")
            val knownMove: Any?,
            @SerializedName("known_move_type")
            val knownMoveType: Any?,
            @SerializedName("location")
            val location: Any?,
            @SerializedName("min_affection")
            val minAffection: Any?,
            @SerializedName("min_beauty")
            val minBeauty: Any?,
            @SerializedName("min_happiness")
            val minHappiness: Any?,
            @SerializedName("min_level")
            val minLevel: Int,
            @SerializedName("needs_overworld_rain")
            val needsOverworldRain: Boolean,
            @SerializedName("party_species")
            val partySpecies: Any?,
            @SerializedName("party_type")
            val partyType: Any?,
            @SerializedName("relative_physical_stats")
            val relativePhysicalStats: Any?,
            @SerializedName("time_of_day")
            val timeOfDay: String,
            @SerializedName("trade_species")
            val tradeSpecies: Any?,
            @SerializedName("trigger")
            val trigger: Trigger,
            @SerializedName("turn_upside_down")
            val turnUpsideDown: Boolean
        ) {
            @Keep
            data class Trigger(
                @SerializedName("name")
                val name: String,
                @SerializedName("url")
                val url: String
            )
        }

        @Keep
        data class Species(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }
}