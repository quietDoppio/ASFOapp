package com.example.asfoapp.data.di.modules

import com.example.asfoapp.data.repositories.CategoryRepositoryImpl
import com.example.asfoapp.data.repositories.RecipesRepositoryImpl
import com.example.asfoapp.domain.repositories.CategoryRepository
import com.example.asfoapp.domain.repositories.RecipesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {
    @Binds
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun bindRecipesRepository(impl: RecipesRepositoryImpl): RecipesRepository
}