package io.github.tiagonuneslx.pokeland.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.tiagonuneslx.pokeland.data.model.Pokemon
import io.github.tiagonuneslx.pokeland.data.source.PokemonRepository
import kotlinx.coroutines.*
import timber.log.Timber

class PokemonPagingSource(
    private val repository: PokemonRepository,
    private val pokeAPI: PokeAPI,
    private val languageName: String,
) : PagingSource<Int, Pokemon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> =
        withContext(Dispatchers.IO) {
            try {
                val limit = params.loadSize
                val offset = params.key ?: 0
                val page = pokeAPI.getPokemonSpeciesList(limit, offset)
                val pokemon = page.results.map {
                    async { repository.getPokemon(it.name, languageName) }
                }.awaitAll().filterNotNull()
                LoadResult.Page(
                    data = pokemon,
                    prevKey = page.previous?.offset,
                    nextKey = page.next?.offset,
                    itemsBefore = offset,
                    itemsAfter = page.count - (offset + page.results.size)
                )
            } catch (e: Exception) {
                Timber.e(e)
                LoadResult.Error(e)
            }
        }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? =
        state.anchorPosition

    private val String.offset
        get() = """\?.*offset=(?<offset>\d+)""".toRegex()
            .find(this)?.groups?.get("offset")?.value?.toIntOrNull()
}