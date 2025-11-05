package com.JoaoPaulo.pokedexapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.JoaoPaulo.pokedexapp.data.remote.PokemonResult
import com.JoaoPaulo.pokedexapp.viewmodel.PokemonListViewModel

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel,
    modifier: Modifier = Modifier,
    onPokemonClick: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val lazyPokemonItems = viewModel.pokemonList.collectAsLazyPagingItems()

    Column(modifier = modifier.fillMaxSize()) {

        TextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            placeholder = { Text("Buscar Pokémon...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true
        )

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(
                    count = lazyPokemonItems.itemCount,
                    key = { index -> lazyPokemonItems[index]?.name ?: index.toString() }
                ) { index ->
                    val pokemon = lazyPokemonItems[index]
                    pokemon?.let {
                        PokemonItem(
                            pokemon = it,
                            onClick = { onPokemonClick(it.name) }
                        )
                    }
                }

                if (lazyPokemonItems.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                    }
                }
            }


            val loadState = lazyPokemonItems.loadState.refresh


            if (loadState is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }


            if (loadState is LoadState.Error) {
                val error = loadState.error
                Text(
                    text = "Erro: ${error.localizedMessage}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }


            if (
                searchQuery.isNotBlank() &&
                loadState is LoadState.NotLoading &&
                lazyPokemonItems.itemCount == 0
            ) {
                Text(
                    text = "Nenhum Pokémon encontrado com \"$searchQuery\"",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

@Composable
fun PokemonItem(pokemon: PokemonResult, onClick: () -> Unit) {

    val id = idFromUrl(pokemon.url) ?: 0
    val imageUrl =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = pokemon.name,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "#${id} ${pokemon.name.replaceFirstChar { it.uppercase() }}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun idFromUrl(url: String): Int? {

    return url.trimEnd('/').substringAfterLast('/').toIntOrNull()
}