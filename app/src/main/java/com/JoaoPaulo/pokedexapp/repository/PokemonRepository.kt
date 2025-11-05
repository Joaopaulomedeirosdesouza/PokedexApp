package com.JoaoPaulo.pokedexapp.repository

import androidx.paging.PagingData
import com.JoaoPaulo.pokedexapp.data.remote.PokemonDetailsResponse
import com.JoaoPaulo.pokedexapp.data.remote.PokemonResult
import kotlinx.coroutines.flow.Flow


interface PokemonRepository {


    fun getPokemonList(query: String): Flow<PagingData<PokemonResult>>


    suspend fun getPokemonDetails(name: String): PokemonDetailsResponse
}