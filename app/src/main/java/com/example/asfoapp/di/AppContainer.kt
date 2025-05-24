package com.example.asfoapp.di

import android.content.Context
import androidx.room.Room
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.AppDatabase
import com.example.asfoapp.data.database.CategoryDao
import com.example.asfoapp.data.repositories.CategoryRepository
import com.example.asfoapp.interfaces.SafeRequestExecutorImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppContainer(context: Context) {

    private val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private val recipeApiService: RecipeApiService by lazy { retrofit.create(RecipeApiService::class.java) }

    private val executor: SafeRequestExecutorImpl by lazy { SafeRequestExecutorImpl() }

    private val categoryDao: CategoryDao by lazy { database.categoryDao() }

    // TODO: private val recipesDap: RecipesDao by lazy { database.recipesDao() }

    val categoryRepository by lazy { CategoryRepository(categoryDao, recipeApiService, executor) }

    // TODO: val recipesRepository by lazy { RecipesRepository(recipesDao, recipeApiService, executor) }
}