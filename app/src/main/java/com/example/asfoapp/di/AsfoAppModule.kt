package com.example.asfoapp.di

import android.content.Context
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.AppDatabase
import com.example.asfoapp.data.database.CategoryDao
import com.example.asfoapp.data.database.RecipeDao
import com.example.asfoapp.data.repositories.CategoryRepository
import com.example.asfoapp.data.repositories.RecipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AsfoAppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl(Constants.API_BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService =
        retrofit.create(RecipeApiService::class.java)

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideRecipeDao(database: AppDatabase): RecipeDao = database.recipeDao()

    @Provides
    fun provideGlideRequestListener(): GlideRequestListener =
        GlideRequestListener()

    @Provides
    fun provideCategoryRepository(
        categoryDao: CategoryDao,
        recipeApiService: RecipeApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CategoryRepository = CategoryRepository(categoryDao, recipeApiService, ioDispatcher)

    @Provides
    fun provideRecipesRepository(
        recipeDao: RecipeDao,
        recipeApiService: RecipeApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RecipesRepository = RecipesRepository(recipeDao, recipeApiService, ioDispatcher)
}