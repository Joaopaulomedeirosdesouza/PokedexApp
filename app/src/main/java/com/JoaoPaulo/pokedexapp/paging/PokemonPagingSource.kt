package com.JoaoPaulo.pokedexapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.JoaoPaulo.pokedexapp.data.remote.PokeApiService
import com.JoaoPaulo.pokedexapp.data.remote.PokemonResult
import retrofit2.HttpException
import java.io.IOException


private const val PAGE_SIZE = 20


private const val MAX_POKEMONS = 150

class PokemonPagingSource(
    private val api: PokeApiService
) : PagingSource<Int, PokemonResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonResult> {
        return try {
            val currentOffset = params.key ?: 0
            val requestedLimit = params.loadSize.coerceAtMost(PAGE_SIZE)


            if (currentOffset >= MAX_POKEMONS) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (currentOffset == 0) null else (currentOffset - requestedLimit).coerceAtLeast(
                        0
                    ),
                    nextKey = null
                )
            }


            val remaining = MAX_POKEMONS - currentOffset
            val actualLimit = minOf(requestedLimit, remaining)


            val response = api.getPokemonList(limit = actualLimit, offset = currentOffset)
            val pokemons = response.results

            val prevKey =
                if (currentOffset == 0) null else (currentOffset - requestedLimit).coerceAtLeast(0)


            val nextOffset = currentOffset + actualLimit
            val nextKey = if (nextOffset >= MAX_POKEMONS || pokemons.isEmpty()) null else nextOffset

            LoadResult.Page(
                data = pokemons,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.plus(PAGE_SIZE) ?: page?.nextKey?.minus(PAGE_SIZE)
        }
    }
}
