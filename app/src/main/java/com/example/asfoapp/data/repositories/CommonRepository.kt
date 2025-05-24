package com.example.asfoapp.data.repositories

import android.util.Log
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.api.RecipeApiService
import com.example.asfoapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object CommonRepository {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val recipesByCategoryIdCache = mutableMapOf<Int, List<Recipe>>()
    private val recipeByIdCache = mutableMapOf<Int, Recipe>()
    private val recipesListByIdsCache = mutableMapOf<String, List<Recipe>>()

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        recipesByCategoryIdCache[id]?.let { return it }

        val result = safeExecuteRequest { service.getRecipesByCategoryId(id) }
        if (result != null) recipesByCategoryIdCache[id] = result
        return result
    }

    suspend fun getRecipes(ids: List<String>): List<Recipe>? {
        val joinedIds = ids.joinToString(",")
        recipesListByIdsCache[joinedIds]?.let { return it }

        val result = safeExecuteRequest { service.getRecipes(joinedIds) }
        if (result != null) recipesListByIdsCache[joinedIds] = result
        return result
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        recipeByIdCache[id]?.let { return it }

        val result = safeExecuteRequest { service.getRecipeById(id) }
        if (result != null) recipeByIdCache[id] = result
        return result
    }

    private suspend fun <T> safeExecuteRequest(task: suspend () -> T): T? {
        val callerFunName = Throwable().stackTrace[1].methodName
        return withContext(Dispatchers.IO) {
            try {
                task()
            } catch (e: Exception) {
                Log.e(
                    Constants.LOG_TAG,
                    "$callerFunName - Ошибка загрузки данных, ${Log.getStackTraceString(e)}"
                )
                null
            }
        }
    }
}