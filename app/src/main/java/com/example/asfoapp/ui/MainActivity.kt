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
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("_binding for ActivityMainBinding must not be null")
    private val threadPool = Executors.newFixedThreadPool(10)
    private val lockObject = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("!!!", "метод onCreate выполняется на потоке: ${Thread.currentThread().name} ")

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        threadPool.execute {
            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}} ")
            val urlPath = "https://recipes.androidsprint.ru/api"
            val getCategoriesUrl = URL("$urlPath/category")

            val connection: HttpURLConnection =
                getCategoriesUrl.openConnection() as HttpURLConnection
            connection.connect()
            val categoriesResponseBody = connection.inputStream.bufferedReader().readText()
            Log.i("!!!", "body: $categoriesResponseBody")
            connection.disconnect()

            val categoryList = Json.decodeFromString<List<Category>>(categoriesResponseBody)
            val categoryIds = categoryList.map { it.id }
            val recipesList: MutableMap<Int, List<Recipe>> = mutableMapOf()

            val latch = CountDownLatch(categoryIds.size)
            categoryIds.forEach { id ->
                threadPool.execute{
                    try {
                        val taskConnection =
                            URL("$urlPath/category/$id/recipes").openConnection() as HttpURLConnection
                        taskConnection.connect()

                        val recipesListResponseBody =
                            taskConnection.inputStream.bufferedReader().readText()

                        val recipes = Json.decodeFromString<List<Recipe>>(recipesListResponseBody)

                        synchronized(lockObject) {
                            recipesList[id] = recipes
                        }

                        taskConnection.disconnect()
                        latch.countDown()
                    } catch (e: Exception) {
                        Log.e("!!!", "Ошибка при получении рецепта из категории $id: ${e.message}")
                    }
                }
            }

            latch.await()

            val recipesListString = buildString {
                for ((categoryId, recipes) in recipesList) {
                    val titles = recipes.joinToString(", ") { it.title }
                    append("category id - $categoryId, $titles\n")
                }
            }

            Log.i("!!!", "categories list: ${categoryList.joinToString(", ") { it.title }}")
            Log.i("!!!", recipesListString)
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