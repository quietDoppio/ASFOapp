package com.example.asfoapp.data

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.asfoapp.model.Category
import com.example.asfoapp.model.Recipe
import com.example.asfoapp.ui.categories.TAG
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

object RecipeRepository {

    private val threadPool = Executors.newFixedThreadPool(10)
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun getCategories(): List<Category>? {
        return submitInPool {
            val call: Call<List<Category>> = service.getCategories()
            val response: Response<List<Category>> = call.execute()
            if (response.isSuccessful) {
                response.body() ?: run {
                    Log.e("!!!", "getCategories: Пустое тело ответа")
                    null
                }
            } else {
                Log.i("!!!", "getCategories: ошибка при запросе категорий")
                response.errorBody()?.close()
                null
            }
        }
    }

    fun getCategoryById(id: Int): Category? {
        return submitInPool {
            val call: Call<Category> = service.getCategoryById(id)
            val response: Response<Category> = call.execute()
            if (response.isSuccessful) {
                response.body() ?: run {
                    Log.e("!!!", "getCategoryById: Пустое тело ответа")
                    null
                }
            } else {
                Log.i("!!!", "getCategoryById: ошибка при запросе категории")
                response.errorBody()?.close()
                null
            }
        }
    }

    fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        return submitInPool {
            val call: Call<List<Recipe>> = service.getRecipesByCategoryId(id)
            val response: Response<List<Recipe>> = call.execute()
            if (response.isSuccessful) {
                response.body() ?: run {
                    Log.e("!!!", "getRecipeByCategoryId: Пустое тело ответа")
                    null
                }
            } else {
                Log.i("!!!", "getRecipesByCategoryId: ошибка при запросе рецептов")
                response.errorBody()?.close()
                null
            }
        }
    }

    fun getRecipes(ids: List<String>): List<Recipe>? {
        return submitInPool {
            val call: Call<List<Recipe>> = service.getRecipes(ids)
            val response: Response<List<Recipe>> = call.execute()
            if (response.isSuccessful) {
                response.body() ?: run {
                    Log.e("!!!", "getRecipes: Пустое тело ответа")
                    null
                }
            } else {
                Log.i("!!!", "getRecipes: ошибка при запросе рецептов")
                response.errorBody()?.close()
                null
            }
        }
    }

    fun getRecipeById(id: Int, callback: (Recipe?) -> Unit) {
        threadPool.execute {
            val recipe: Recipe? = try {
                val call: Call<Recipe> = service.getRecipeById(id)
                val response: Response<Recipe> = call.execute()
                if (response.isSuccessful) {
                    response.body() ?: run {
                        Log.e("!!!", "getRecipeById: Пустое тело ответа")
                        null
                    }
                } else {
                    Log.i("!!!", "getRecipeById: ошибка при запросе рецепта. ${response.message()}")
                    response.errorBody()?.close()
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "getRecipeById: ${Log.getStackTraceString(e)}")
                null
            }
            handler.post{
                callback(recipe)
            }
        }
    }

    /*  private fun <T> submitInPool(task: () -> T): T? {
          return try {
              val future: Future<T> = threadPool.submit(Callable { task() })
              future.get(8, TimeUnit.SECONDS)
          } catch (e: Exception) {
              Log.e("!!!", "submitInPool: request error. ${Log.getStackTraceString(e)}")
              null
          }
      } */
}


