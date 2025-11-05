package com.JoaoPaulo.pokedexapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.JoaoPaulo.pokedexapp.data.remote.PokemonResult
import com.JoaoPaulo.pokedexapp.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


    val pokemonList: Flow<PagingData<PokemonResult>> = _searchQuery
        .flatMapLatest { query ->

            repository.getPokemonList(query)
        }
        .cachedIn(viewModelScope)


    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}