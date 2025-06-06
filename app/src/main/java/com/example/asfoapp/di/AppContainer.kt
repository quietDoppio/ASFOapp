package com.example.asfoapp.di

import android.content.Context
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.AppDatabase
import com.example.asfoapp.data.database.CategoryDao
import com.example.asfoapp.data.database.RecipeDao
import com.example.asfoapp.data.repositories.CategoryRepository
import com.example.asfoapp.data.repositories.RecipesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppContainer(context: Context) {

    private val database by lazy { AppDatabase.getDatabase(context) }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()
    }

    private val dispatcherIO: CoroutineDispatcher by lazy { Dispatchers.IO }

    private val recipeApiService: RecipeApiService by lazy { retrofit.create(RecipeApiService::class.java) }

    private val categoryDao: CategoryDao by lazy { database.categoryDao() }

    private val recipesDao: RecipeDao by lazy { database.recipeDao() }

    val glideRequestListener: GlideRequestListener by lazy {
        GlideRequestListener()
    }

    val categoryRepository by lazy {
        CategoryRepository(categoryDao, recipeApiService, dispatcherIO)
    }

    val recipesRepository by lazy { RecipesRepository(recipesDao, recipeApiService, dispatcherIO) }
}