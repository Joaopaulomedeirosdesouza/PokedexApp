package com.JoaoPaulo.pokedexapp.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.JoaoPaulo.pokedexapp.data.remote.PokemonDetailsResponse
import com.JoaoPaulo.pokedexapp.ui.screens.detail.components.InfoItem
import com.JoaoPaulo.pokedexapp.ui.screens.detail.components.StatItem
import com.JoaoPaulo.pokedexapp.ui.screens.detail.components.TypeChip
import com.JoaoPaulo.pokedexapp.ui.utils.formatHeight
import com.JoaoPaulo.pokedexapp.ui.utils.formatWeight
import com.JoaoPaulo.pokedexapp.ui.utils.translateAndColorType
import com.JoaoPaulo.pokedexapp.ui.utils.translateStat
import java.util.Locale

@Composable
fun PokemonDetailContent(pokemon: PokemonDetailsResponse) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Image(
            painter = rememberAsyncImagePainter(pokemon.sprites.other.officialArtwork.front_default),
            contentDescription = pokemon.name,
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "#${pokemon.id} ${pokemon.name.uppercase(Locale.ROOT)}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pokemon.types.forEach { typeSlot ->
                val (translatedType, typeColor) = translateAndColorType(typeSlot.type.name)
                TypeChip(text = translatedType, color = typeColor)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            InfoItem(title = "Peso", value = formatWeight(pokemon.weight))
            InfoItem(title = "Altura", value = formatHeight(pokemon.height))
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Base Stats",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pokemon.stats.forEach { statSlot ->
                StatItem(
                    statName = translateStat(statSlot.stat.name),
                    statValue = statSlot.base_stat
                )
            }
        }
    }
}