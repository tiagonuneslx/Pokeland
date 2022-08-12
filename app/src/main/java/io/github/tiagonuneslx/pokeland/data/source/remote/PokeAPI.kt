package io.github.tiagonuneslx.pokeland.data.source.remote

import io.github.tiagonuneslx.pokeland.data.source.remote.response.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeAPI {

    companion object {
        const val BaseUrl = "https://pokeapi.co/api/v2/"
    }

    @GET("pokemon-species")
    suspend fun getPokemonSpeciesList(@Query("limit") limit: Int, @Query("offset") offset: Int): ListResponse

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpecies(@Path("name") name: String): GetPokemonSpeciesResponse

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): GetPokemonResponse

    @GET("move/{name}")
    suspend fun getMove(@Path("name") name: String): GetMoveResponse

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(@Path("id") id: Int): GetEvolutionChainResponse
}