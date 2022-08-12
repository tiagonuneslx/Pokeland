package io.github.tiagonuneslx.pokeland.ui.screen.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.tiagonuneslx.pokeland.R
import io.github.tiagonuneslx.pokeland.data.source.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    repository: PokemonRepository,
) : ViewModel() {

    val pokemonPagedFlow = repository.getPokemonPagedFlow(appContext.getString(R.string.language_name))
        .cachedIn(viewModelScope)

    private val _shouldShowStaticPlaceholdersFlow = MutableStateFlow(true)
    val shouldShowStaticPlaceholdersFlow = _shouldShowStaticPlaceholdersFlow.asStateFlow()

    suspend fun stopShowingStaticPlaceholders() {
        _shouldShowStaticPlaceholdersFlow.emit(false)
    }
}