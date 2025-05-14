package com.example.asfoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.example.asfoapp.R
import com.example.asfoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("_binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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