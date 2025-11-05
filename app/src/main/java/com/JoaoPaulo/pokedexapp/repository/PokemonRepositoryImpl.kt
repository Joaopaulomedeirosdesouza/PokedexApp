package com.JoaoPaulo.pokedexapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.JoaoPaulo.pokedexapp.data.paging.PokemonPagingSource
import com.JoaoPaulo.pokedexapp.data.paging.PokemonSearchPagingSource
import com.JoaoPaulo.pokedexapp.data.remote.PokeApiService
import com.JoaoPaulo.pokedexapp.data.remote.PokemonDetailsResponse
import com.JoaoPaulo.pokedexapp.data.remote.PokemonResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApiService
) : PokemonRepository {


    override fun getPokemonList(query: String): Flow<PagingData<PokemonResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {

                if (query.isBlank()) {

                    PokemonPagingSource(api)
                } else {

                    PokemonSearchPagingSource(api, query)
                }
            }
        ).flow
    }


    override suspend fun getPokemonDetails(name: String): PokemonDetailsResponse {
        val response = api.getPokemonDetails(name)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Falha ao buscar detalhes: ${response.code()} ${response.message()}")
        }
    }
}