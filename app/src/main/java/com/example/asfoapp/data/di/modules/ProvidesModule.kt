package com.example.asfoapp.data.di.modules

import android.content.Context
import com.example.asfoapp.data.api.NetworkConfig
import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.data.database.AppDatabase
import com.example.asfoapp.data.database.dao.CategoryDao
import com.example.asfoapp.data.database.dao.RecipeDao
import com.example.asfoapp.data.di.IoDispatcher
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
class ProvidesModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideRecipeDao(database: AppDatabase): RecipeDao = database.recipeDao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl(NetworkConfig.API_BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()

    @Provides
    @Singleton
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService =
        retrofit.create(RecipeApiService::class.java)

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
