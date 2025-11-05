package com.JoaoPaulo.pokedexapp.ui.utils


import com.google.common.truth.Truth.assertThat

import org.junit.Test


class PokemonDetailUtilsKtTest {


    @Test
    fun `formatWeight converte hectogramas para kg corretamente`() {

        val weightInHectograms = 100


        val result = formatWeight(weightInHectograms)


        assertThat(result).isEqualTo("10.0 kg")
    }

    @Test
    fun `formatWeight lida com valores quebrados`() {

        val weightInHectograms = 65 // 65 hg = 6.5 kg


        val result = formatWeight(weightInHectograms)


        assertThat(result).isEqualTo("6.5 kg")
    }

    @Test
    fun `formatHeight converte decímetros para metros corretamente`() {

        val heightInDecimeters = 17 // 17 dm = 1.7 m


        val result = formatHeight(heightInDecimeters)


        assertThat(result).isEqualTo("1.7 m")
    }

    @Test
    fun `translateStat traduz nomes conhecidos`() {
        assertThat(translateStat("hp")).isEqualTo("HP")
        assertThat(translateStat("attack")).isEqualTo("Ataque")
        assertThat(translateStat("special-defense")).isEqualTo("Sp. Def")
    }

    @Test
    fun `translateStat retorna nome capitalizado para stat desconhecido`() {
        // Testa o 'else' do nosso 'when'
        assertThat(translateStat("unknown-stat")).isEqualTo("Unknown-stat")
    }

    @Test
    fun `translateAndColorType traduz tipo Fogo`() {
        val (name, _) = translateAndColorType("fire")

        assertThat(name).isEqualTo("Fogo")

    }

    @Test
    fun `translateAndColorType retorna cinza para tipo desconhecido`() {
        val (name, color) = translateAndColorType("unknown-type")

        assertThat(name).isEqualTo("Unknown-type")

        assertThat(color).isEqualTo(androidx.compose.ui.graphics.Color.Gray)
    }
}