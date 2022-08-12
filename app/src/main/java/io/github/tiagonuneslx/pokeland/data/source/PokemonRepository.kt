package io.github.tiagonuneslx.pokeland.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.tiagonuneslx.pokeland.data.model.Pokemon
import io.github.tiagonuneslx.pokeland.data.source.remote.PokeAPI
import io.github.tiagonuneslx.pokeland.data.source.remote.PokemonPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val pokeAPI: PokeAPI
) {

    fun getPokemonPagedFlow(languageName: String): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 21,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = { PokemonPagingSource(this, pokeAPI, languageName) }
        ).flow
    }

    suspend fun getPokemon(name: String, languageName: String): Pokemon? = withContext(Dispatchers.IO) {
        val pokemonSpecies = try {
            pokeAPI.getPokemonSpecies(name)
        } catch (e: Exception) {
            Timber.e(e)
            return@withContext null
        }
        val pokemon = try {
            val pokemonName = pokemonSpecies.varieties.first { it.isDefault }.pokemon.name
            pokeAPI.getPokemon(pokemonName)
        } catch (e: Exception) {
            Timber.e(e)
            return@withContext null
        }
        return@withContext Pokemon(
            id = pokemonSpecies.name,
            number = pokemonSpecies.id,
            name = pokemonSpecies.names.first { it.language.name == languageName }.name,
            description = pokemonSpecies.flavorTextEntries.firstOrNull { it.language.name == languageName }?.flavorText
                ?.replace("\n", " "),
            imageUrl = pokemon.sprites.other?.officialArtwork?.frontDefault
                ?: pokemon.sprites.frontDefault,
            types = pokemon.types
                .sortedBy { it.slot }
                .map { Pokemon.Type.getOrUnknown(it.type.name) },
            height = pokemon.height / 10f, // dm -> m
            weight = pokemon.weight / 10f, // hm -> km
            abilitySlots = pokemon.abilities
                .sortedBy { it.slot }
                .map { it.ability.name },
            baseExperience = pokemon.baseExperience,
            moveSlots = pokemon.moves.map { it.move.name },
            stats = Pokemon.Stats(
                hp = pokemon.stats.firstOrNull { it.stat.name == "hp" }?.baseStat ?: 0,
                attack = pokemon.stats.firstOrNull { it.stat.name == "attack" }?.baseStat ?: 0,
                defense = pokemon.stats.firstOrNull { it.stat.name == "defense" }?.baseStat ?: 0,
                specialAttack = pokemon.stats.firstOrNull { it.stat.name == "special-attack" }?.baseStat
                    ?: 0,
                specialDefense = pokemon.stats.firstOrNull { it.stat.name == "special-defense" }?.baseStat
                    ?: 0,
                speed = pokemon.stats.firstOrNull { it.stat.name == "speed" }?.baseStat ?: 0,
            ),
            evolutionChainId = pokemonSpecies.evolutionChain?.let {
                """evolution-chain/(?<id>\d+)""".toRegex()
                    .find(pokemonSpecies.evolutionChain.url)?.groups?.get("id")?.value?.toIntOrNull()
            }
        )
    }

    suspend fun getMove(name: String, languageName: String) = withContext(Dispatchers.IO) {
        try {
            pokeAPI.getMove(name).let {
                Pokemon.Move(
                    id = it.name,
                    name = it.names.first { it.language.name == languageName }.name,
                    accuracy = it.accuracy.takeIf { it > 0 },
                    description = it.flavorTextEntries.first { it.language.name == languageName }.flavorText
                        .replace("\n", " "),
                    power = it.power,
                    pp = it.pp,
                    type = Pokemon.Type.getOrUnknown(it.type.name)
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    suspend fun getEvolutionChain(id: Int) = withContext(Dispatchers.IO) {
        try {
            pokeAPI.getEvolutionChain(id)
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}