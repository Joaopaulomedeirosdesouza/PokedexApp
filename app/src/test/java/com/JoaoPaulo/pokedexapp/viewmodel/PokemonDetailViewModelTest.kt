package com.JoaoPaulo.pokedexapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.JoaoPaulo.pokedexapp.data.remote.OfficialArtwork
import com.JoaoPaulo.pokedexapp.data.remote.OtherSprites
import com.JoaoPaulo.pokedexapp.data.remote.PokemonDetailsResponse
import com.JoaoPaulo.pokedexapp.data.remote.PokemonStat
import com.JoaoPaulo.pokedexapp.data.remote.PokemonStatSlot
import com.JoaoPaulo.pokedexapp.data.remote.PokemonType
import com.JoaoPaulo.pokedexapp.data.remote.PokemonTypeSlot
import com.JoaoPaulo.pokedexapp.data.remote.Sprites
import com.JoaoPaulo.pokedexapp.repository.PokemonRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class PokemonDetailViewModelTest {


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PokemonRepository
    private lateinit var viewModel: PokemonDetailViewModel


    @Before
    fun setUp() {

        Dispatchers.setMain(testDispatcher)


        repository = mockk(relaxed = true)


        viewModel = PokemonDetailViewModel(repository)
    }

    @After
    fun tearDown() {

        Dispatchers.resetMain()
    }


    @Test
    fun `loadPokemonDetail com sucesso atualiza o StateFlow do pokemon`() =
        runTest(testDispatcher) {

            val fakePokemon = createFakePokemonDetails("pikachu")


            coEvery { repository.getPokemonDetails("pikachu") } returns fakePokemon


            viewModel.loadPokemonDetail("pikachu")


            assertThat(viewModel.isLoading.value).isTrue()


            testDispatcher.scheduler.advanceUntilIdle()


            assertThat(viewModel.isLoading.value).isFalse()
            assertThat(viewModel.errorMessage.value).isNull()
            assertThat(viewModel.pokemon.value).isEqualTo(fakePokemon)
        }

    @Test
    fun `loadPokemonDetail com erro 404 atualiza o errorMessage`() = runTest(testDispatcher) {

        val errorMessage = "Pokémon 'mewthree' não encontrado"
        val fakeException = Exception(errorMessage)


        coEvery { repository.getPokemonDetails("mewthree") } throws fakeException


        viewModel.loadPokemonDetail("mewthree")


        assertThat(viewModel.isLoading.value).isTrue()


        testDispatcher.scheduler.advanceUntilIdle()


        assertThat(viewModel.isLoading.value).isFalse()
        assertThat(viewModel.pokemon.value).isNull()
        assertThat(viewModel.errorMessage.value).contains(errorMessage)
    }


    private fun createFakePokemonDetails(name: String): PokemonDetailsResponse {
        return PokemonDetailsResponse(
            id = 25,
            name = name,
            height = 4,
            weight = 60,
            sprites = Sprites(
                other = OtherSprites(
                    officialArtwork = OfficialArtwork("url_fake")
                )
            ),
            types = listOf(
                PokemonTypeSlot(type = PokemonType("electric"))
            ),
            stats = listOf(
                PokemonStatSlot(base_stat = 90, stat = PokemonStat("speed"))
            )
        )
    }
}
