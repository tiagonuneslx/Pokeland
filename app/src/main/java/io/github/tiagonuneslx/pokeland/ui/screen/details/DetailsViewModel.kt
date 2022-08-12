package io.github.tiagonuneslx.pokeland.ui.screen.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.tiagonuneslx.pokeland.R
import io.github.tiagonuneslx.pokeland.data.model.Pokemon
import io.github.tiagonuneslx.pokeland.data.source.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val repository: PokemonRepository,
) : ViewModel() {

    private var pokemonFlow: MutableStateFlow<Pokemon?> = MutableStateFlow(null)

    val movesFlow = pokemonFlow.filterNotNull().map { pokemon ->
        coroutineScope {
            pokemon.moveSlots.map {
                async { repository.getMove(it, appContext.getString(R.string.language_name)) }
            }.awaitAll().filterNotNull()
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val evolutionsFlow = pokemonFlow.filterNotNull().map { pokemon ->
        pokemon.evolutionChainId ?: return@map emptyList<Pokemon.Evolution>()
        val chainStart = repository.getEvolutionChain(pokemon.evolutionChainId)?.chain
            ?: return@map emptyList<Pokemon.Evolution>()
        val evolutions = mutableListOf<Pokemon.Evolution>()
        val queue = ArrayDeque(listOf(chainStart))
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            current.evolvesTo.forEach { evolution ->
                queue.addLast(evolution)
                if(current.species.name != pokemon.id) return@forEach
                val evolutionPokemon = repository.getPokemon(evolution.species.name, appContext.getString(R.string.language_name))
                    ?: return@forEach
                evolutions.add(
                    Pokemon.Evolution(
                        evolutionPokemon,
                        evolution.evolutionDetails.firstOrNull()?.minLevel
                    )
                )
            }
        }
        evolutions.toList()
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Unfortunately, we can't pass the SharedViewModel.pokemonFlow reference to this viewModel,
    // because @AssistedInject is not yet supported using a @HiltViewModel annotated ViewModel:
    // https://github.com/google/dagger/issues/2287
    fun setPokemon(pokemon: Pokemon) {
        pokemonFlow.value = pokemon
    }
}