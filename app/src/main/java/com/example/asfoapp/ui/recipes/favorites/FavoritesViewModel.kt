package com.example.asfoapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asfoapp.data.RecipeRepository
import com.example.asfoapp.model.Recipe
import com.example.asfoapp.ui.NET_ERROR_MESSAGE
import com.example.asfoapp.ui.recipes.recipe.ASFOAPP_PREFS_FILE_KEY
import com.example.asfoapp.ui.recipes.recipe.FAVORITES_PREFS_KEY

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _favoritesState: MutableLiveData<FavoritesState> = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadRecipes() {
        val safeStateCopy = favoritesState.value?.copy()
        RecipeRepository.getRecipes(getFavoritesIds()) { recipes ->
            if (recipes == null) {
                _toastMessage.postValue(NET_ERROR_MESSAGE)
            } else {
                _favoritesState.postValue(safeStateCopy?.copy(favoritesRecipes = recipes))
            }

        }
    }

    private fun getFavoritesIds(): MutableSet<String> {
        val sharedPrefs = application.applicationContext.getSharedPreferences(
            ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_PREFS_KEY, emptySet()) ?: emptySet())
    }

    data class FavoritesState(
        val favoritesRecipes: List<Recipe> = emptyList()
    )
}