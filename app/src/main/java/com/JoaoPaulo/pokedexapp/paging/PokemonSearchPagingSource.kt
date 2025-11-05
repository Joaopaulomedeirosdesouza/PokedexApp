package com.JoaoPaulo.pokedexapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.JoaoPaulo.pokedexapp.data.remote.PokeApiService
import com.JoaoPaulo.pokedexapp.data.remote.PokemonResult
import retrofit2.HttpException
import java.io.IOException


private const val MAX_POKEMONS = 150

private const val SEARCH_PAGE_SIZE = 20

class PokemonSearchPagingSource(
    private val api: PokeApiService,
    private val query: String
) : PagingSource<Int, PokemonResult>() {


    companion object {
        private var fullPokemonListCache: List<PokemonResult>? = null
    }


    private suspend fun fetchAndCacheFullList(): List<PokemonResult> {

        fullPokemonListCache?.let { return it }


        return try {
            val response = api.getPokemonList(limit = MAX_POKEMONS, offset = 0)
            val list = response.results
            fullPokemonListCache = list
            list
        } catch (e: Exception) {


            emptyList()
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonResult> {
        try {

            val fullList = fetchAndCacheFullList()


            val filteredList = fullList.filter {
                it.name.contains(query, ignoreCase = true)
            }


            val currentPage = params.key ?: 0
            val pageSize = params.loadSize.coerceAtMost(SEARCH_PAGE_SIZE)


            val start = currentPage * pageSize

            val end = (start + pageSize).coerceAtMost(filteredList.size)


            val data =
                if (start >= filteredList.size) emptyList() else filteredList.subList(start, end)


            val prevKey = if (currentPage == 0) null else currentPage - 1
            val nextKey = if (end >= filteredList.size) null else currentPage + 1

            return LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonResult>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}