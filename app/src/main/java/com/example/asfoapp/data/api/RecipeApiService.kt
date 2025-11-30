package com.example.asfoapp.data.api

import com.example.asfoapp.data.api.model.CategoryDto
import com.example.asfoapp.data.api.model.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("category")
    suspend fun getCategories(): List<CategoryDto>

    @GET("category/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): CategoryDto

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(@Path("id") id: Int): List<RecipeDto>

    @GET("recipes")
    suspend fun getRecipes(@Query("ids") ids: String): List<RecipeDto>

    @GET("recipe/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): RecipeDto
}
