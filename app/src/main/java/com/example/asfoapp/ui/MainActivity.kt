package com.example.asfoapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.example.asfoapp.R
import com.example.asfoapp.databinding.ActivityMainBinding
import com.example.asfoapp.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("_binding for ActivityMainBinding must not be null")

    private val thread = Thread {
        Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}} ")
        val url = URL("https://recipes.androidsprint.ru/api/category")
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connect()

        val body = connection.inputStream.bufferedReader().readText()
        Log.i("!!!", "body: $body")
        val categoryList = Json.decodeFromString<List<Category>>(body)
        Log.i("!!!", "category list: ${categoryList.joinToString(", ") { it.title }}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("!!!", "метод onCreate выполняется на потоке: ${Thread.currentThread().name} ")

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thread.start()

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
}