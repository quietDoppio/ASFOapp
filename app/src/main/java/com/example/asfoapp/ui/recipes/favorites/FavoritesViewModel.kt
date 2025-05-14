package com.example.asfoapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asfoapp.Constants
import com.example.asfoapp.data.RecipeRepository
import com.example.asfoapp.model.Recipe

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _favoritesState: MutableLiveData<FavoritesState> = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadRecipes() {
        val favoritesIds = getFavoritesIds()
        if (favoritesIds.isNotEmpty()) {
            RecipeRepository.getRecipes(favoritesIds) { recipes ->
                if (recipes == null) {
                    _toastMessage.postValue(Constants.NET_ERROR_MESSAGE)
                } else {
                    _favoritesState.postValue(favoritesState.value?.copy(favoritesRecipes = recipes))
                }

            }
        }
    }

    private fun getFavoritesIds(): MutableSet<String> {
        val sharedPrefs = application.applicationContext.getSharedPreferences(
            Constants.ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(Constants.FAVORITES_PREFS_KEY, emptySet()) ?: emptySet())
    }

    data class FavoritesState(
        val favoritesRecipes: List<Recipe> = emptyList()
    )
}