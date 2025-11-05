package com.JoaoPaulo.pokedexapp.di

import com.JoaoPaulo.pokedexapp.repository.PokemonRepository
import com.JoaoPaulo.pokedexapp.repository.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module

import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindPokemonRepository(
        pokemonRepositoryImpl: PokemonRepositoryImpl
    ): PokemonRepository
}