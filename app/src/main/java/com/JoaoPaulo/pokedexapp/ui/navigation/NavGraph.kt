package com.JoaoPaulo.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.JoaoPaulo.pokedexapp.ui.screens.PokemonDetailScreen
import com.JoaoPaulo.pokedexapp.ui.screens.PokemonListScreen
import com.JoaoPaulo.pokedexapp.viewmodel.PokemonDetailViewModel
import com.JoaoPaulo.pokedexapp.viewmodel.PokemonListViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "pokemon_list"
    ) {

        composable("pokemon_list") {
            val listViewModel: PokemonListViewModel = hiltViewModel()
            PokemonListScreen(
                viewModel = listViewModel,
                onPokemonClick = { pokemonName ->
                    navController.navigate("pokemon_detail/$pokemonName")
                }
            )
        }


        composable("pokemon_detail/{pokemonName}") { backStackEntry ->
            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""
            val detailViewModel: PokemonDetailViewModel = hiltViewModel()


            val pokemon = detailViewModel.pokemon.collectAsState().value
            val isLoading = detailViewModel.isLoading.collectAsState().value
            val errorMessage = detailViewModel.errorMessage.collectAsState().value


            LaunchedEffect(key1 = pokemonName) {
                detailViewModel.loadPokemonDetail(pokemonName)
            }

            PokemonDetailScreen(
                pokemon = pokemon,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onBack = { navController.popBackStack() }
            )


        }
    }
}