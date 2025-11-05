package com.JoaoPaulo.pokedexapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.JoaoPaulo.pokedexapp.data.remote.PokemonDetailsResponse
import com.JoaoPaulo.pokedexapp.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemon = MutableStateFlow<PokemonDetailsResponse?>(null)
    val pokemon: StateFlow<PokemonDetailsResponse?> = _pokemon

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun loadPokemonDetail(nameOrId: String) {


        _isLoading.value = true
        _errorMessage.value = null


        viewModelScope.launch {
            try {

                val pokemonDetails = repository.getPokemonDetails(nameOrId)
                _pokemon.value = pokemonDetails

            } catch (e: Exception) {

                e.printStackTrace()
                _errorMessage.value =
                    "Falha ao carregar detalhes: ${e.localizedMessage ?: "Erro desconhecido"}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun clear() {
        _pokemon.value = null
        _errorMessage.value = null
        _isLoading.value = false
    }
}