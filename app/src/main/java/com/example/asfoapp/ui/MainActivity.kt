package com.example.asfoapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.example.asfoapp.R
import com.example.asfoapp.databinding.ActivityMainBinding
import com.example.asfoapp.model.Category
import com.example.asfoapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("_binding for ActivityMainBinding must not be null")
    private val threadPool = Executors.newFixedThreadPool(10)
    private val monitor = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("!!!", "метод onCreate выполняется на потоке: ${Thread.currentThread().name} ")

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        threadPool.execute {
            val urlPath = "https://recipes.androidsprint.ru/api"

            val interceptor = HttpLoggingInterceptor() { message ->
                Log.i("!!!", message)
            }.apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
            val getCategoryRequest: Request = Request.Builder().url("$urlPath/category").build()

            var categoryList: List<Category> = emptyList()

            try {
                client.newCall(getCategoryRequest).execute().use { response ->
                    response.body?.string()?.let { body ->
                        categoryList = Json.decodeFromString<List<Category>>(body)
                    }
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка при получении категорий: ${e.message}")
            }

            val categoryIds = categoryList.map { it.id }
            val recipesMap: MutableMap<Int, List<Recipe>> = mutableMapOf()

            categoryIds.forEach { id ->
                threadPool.execute {
                    try {
                        val getRecipeRequest: Request = Request.Builder()
                            .url("$urlPath/category/$id/recipes")
                            .build()
                        client.newCall(getRecipeRequest).execute().use { response ->
                            response.body?.string()?.let { body ->
                                val recipes = Json.decodeFromString<List<Recipe>>(body)
                                synchronized(monitor) {
                                    recipesMap[id] = recipes
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("!!!", "Ошибка при получении рецепта из категории $id: ${e.message}")
                    }
                }
            }
        }

        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val animOption = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        binding.buttonFavorites.setOnClickListener {
            if (navController.currentDestination?.id != R.id.favoritesFragment) {
                navController.popBackStack(R.id.categoriesListFragment, false)
                navController.navigate(R.id.favoritesFragment, args = null, navOptions = animOption)
            }
        }
        binding.buttonCategories.setOnClickListener {
            if (navController.currentDestination?.id != R.id.categoriesListFragment) {
                navController.popBackStack(R.id.categoriesListFragment, true)
                navController.navigate(
                    R.id.categoriesListFragment,
                    args = null,
                    navOptions = animOption
                )
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}