package com.JoaoPaulo.pokedexapp.ui.utils

import androidx.compose.ui.graphics.Color


fun formatWeight(weight: Int): String {
    val kg = weight / 10f
    return "$kg kg"
}


fun formatHeight(height: Int): String {
    val meters = height / 10f
    return "$meters m"
}


fun translateStat(stat: String): String {
    return when (stat) {
        "hp" -> "HP"
        "attack" -> "Ataque"
        "defense" -> "Defesa"
        "special-attack" -> "Sp. Atk"
        "special-defense" -> "Sp. Def"
        "speed" -> "Velocidade"
        else -> stat.replaceFirstChar { it.uppercase() }
    }
}


fun translateAndColorType(type: String): Pair<String, Color> {
    return when (type) {
        "normal" -> "Normal" to Color(0xFFA8A77A)
        "fire" -> "Fogo" to Color(0xFFEE8130)
        "water" -> "Água" to Color(0xFF6390F0)
        "electric" -> "Elétrico" to Color(0xFFF7D02C)
        "grass" -> "Planta" to Color(0xFF7AC74C)
        "ice" -> "Gelo" to Color(0xFF96D9D6)
        "fighting" -> "Lutador" to Color(0xFFC22E28)
        "poison" -> "Veneno" to Color(0xFFA33EA1)
        "ground" -> "Terra" to Color(0xFFE2BF65)
        "flying" -> "Voador" to Color(0xFFA98FF3)
        "psychic" -> "Psíquico" to Color(0xFFF95587)
        "bug" -> "Inseto" to Color(0xFFA6B91A)
        "rock" -> "Pedra" to Color(0xFFB6A136)
        "ghost" -> "Fantasma" to Color(0xFF735797)
        "dragon" -> "Dragão" to Color(0xFF6F35FC)
        "dark" -> "Sombrio" to Color(0xFF705746)
        "steel" -> "Aço" to Color(0xFFB7B7CE)
        "fairy" -> "Fada" to Color(0xFFD685AD)
        else -> type.replaceFirstChar { it.uppercase() } to Color.Gray
    }
}