package com.example.asfoapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.repositories.CommonRepository
import com.example.asfoapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _favoritesState: MutableLiveData<FavoritesState> = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadRecipes() {
        viewModelScope.launch {
            val favoritesIds = getFavoritesIds().toList()
            Log.i(Constants.LOG_TAG, "loadRecipes: favoritesIdsSize - ${favoritesIds.size}")
            if (favoritesIds.isNotEmpty()) {
                val recipes = CommonRepository.getRecipes(favoritesIds)
                Log.i(Constants.LOG_TAG, "loadRecipes: apiRecipesSize - ${recipes?.size}")
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
        return HashSet(
            sharedPrefs?.getStringSet(Constants.FAVORITES_PREFS_KEY, emptySet()) ?: emptySet()
        )
    }

    data class FavoritesState(
        val favoritesRecipes: List<Recipe> = emptyList()
    )
}