package com.example.asfoapp.data

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.asfoapp.model.Category
import com.example.asfoapp.model.Recipe
import com.example.asfoapp.ui.TAG
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.Executors

object RecipeRepository {

    private val threadPool = Executors.newFixedThreadPool(10)
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private var categoriesCache: List<Category>? = null
    private val categoryByIdCache = mutableMapOf<Int, Category>()
    private val recipesByCategoryIdCache = mutableMapOf<Int, List<Recipe>>()
    private val recipeByIdCache = mutableMapOf<Int, Recipe>()
    private val recipesListByIdsCache = mutableMapOf<Set<String>, List<Recipe>>()

    fun getCategories(callback: (List<Category>?) -> Unit) {
        categoriesCache?.let {
            handler.post { callback(it) }
            return
        }
        threadPool.execute{
            val result = executeCall(service.getCategories())
            categoriesCache = result
            handler.post { callback (result) }
        }
    }

    fun getCategoryById(id: Int, callback: (Category?) -> Unit) {
        categoryByIdCache[id]?.let {
            handler.post { callback(it) }
            return
        }
        threadPool.execute {
            val result = executeCall(service.getCategoryById(id))
            result?.let { categoryByIdCache[id] = it }
            handler.post { callback(result) }
        }
    }

    fun getRecipesByCategoryId(id: Int, callback: (List<Recipe>?) -> Unit) {
        recipesByCategoryIdCache[id]?.let {
            handler.post { callback(it) }
            return
        }
        threadPool.execute {
            val result = executeCall(service.getRecipesByCategoryId(id))
            result?.let { recipesByCategoryIdCache[id] = it }
            handler.post { callback(result) }
        }
    }

    fun getRecipes(ids: Set<String>, callback: (List<Recipe>?) -> Unit) {
        recipesListByIdsCache[ids]?.let {
            handler.post { callback(it) }
            return
        }
        threadPool.execute {
            val result = executeCall(service.getRecipes(ids))
            result?.let { recipesListByIdsCache[ids] = it }
            handler.post { callback(result) }
        }
    }


    fun getRecipeById(id: Int, callback: (Recipe?) -> Unit) {
        recipeByIdCache[id]?.let {
            handler.post { callback(it) }
            return
        }
        threadPool.execute {
            val result = executeCall(service.getRecipeById(id))
            result?.let { recipeByIdCache[id] = it }
            handler.post { callback(result) }
        }
    }

    private fun<T> executeCall(call: Call<T>): T? {
        val callerFunName = Throwable().stackTrace[1].methodName
        Log.i(TAG, "!!!: callerFunName - $callerFunName")

        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                response.body() ?: run {
                    Log.i(TAG, "$callerFunName: пустое тело ответа")
                    null
                }
            } else {
                Log.i(TAG, "$callerFunName: ошибка при запросе рецепта. ${response.message()}")
                response.errorBody()?.close()
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "$callerFunName: ${Log.getStackTraceString(e)}")
            null
        }
    }

}


