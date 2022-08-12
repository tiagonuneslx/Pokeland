package io.github.tiagonuneslx.pokeland.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.tiagonuneslx.pokeland.data.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel() {

    private val _selectedPokemonFlow = MutableStateFlow<Pokemon?>(null)
    val selectedPokemonFlow = _selectedPokemonFlow.asStateFlow()

    suspend fun selectPokemon(pokemon: Pokemon?) = withContext(Dispatchers.IO) {
        _selectedPokemonFlow.emit(pokemon)
    }
}