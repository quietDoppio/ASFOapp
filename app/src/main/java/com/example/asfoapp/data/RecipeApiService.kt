package com.example.asfoapp.data

import com.example.asfoapp.model.Category
import com.example.asfoapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: Int): Call<List<Recipe>>

    @GET("recipes")
    fun getRecipes(@Query("ids") ids: Set<String>): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id: Int): Call<Recipe>
}
