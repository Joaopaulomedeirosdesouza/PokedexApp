package com.JoaoPaulo.pokedexapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


data class PokemonListResponse(
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
)


interface PokeApiService {


    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 150,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse


    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name: String
    ): retrofit2.Response<PokemonDetailsResponse>
}


data class PokemonDetailsResponse(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<PokemonTypeSlot>,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonStatSlot>
)


data class Sprites(
    @SerializedName("other")
    val other: OtherSprites
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    val front_default: String?
)


data class PokemonTypeSlot(
    val type: PokemonType
)

data class PokemonType(
    val name: String
)


data class PokemonStatSlot(
    val base_stat: Int,
    val stat: PokemonStat
)

data class PokemonStat(
    val name: String
)