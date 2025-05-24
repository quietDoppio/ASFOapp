package com.example.asfoapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.repositories.CommonRepository
import com.example.asfoapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _recipeState: MutableLiveData<RecipeState> = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipe = CommonRepository.getRecipeById(recipeId)
            if (recipe == null) {
                _toastMessage.postValue(Constants.NET_ERROR_MESSAGE)
            } else {
                val isFavorite = getFavoritesIds().contains(recipeId.toString())
                _recipeState.postValue(
                    recipeState.value?.copy(
                        recipe = recipe,
                        apiHeaderImageUrl = "${Constants.BASE_URL}images/${recipe.imageUrl}",
                        portionsCount = recipeState.value?.portionsCount ?: 1,
                        isFavorite = isFavorite,
                    )
                )
            }
        }
    }

    private fun getFavoritesIds(): MutableSet<String> {
        val sharedPreferences = application.applicationContext.getSharedPreferences(
            Constants.ASFOAPP_PREFS_FILE_KEY, Context.MODE_PRIVATE
        )
        return HashSet(
            sharedPreferences?.getStringSet(Constants.FAVORITES_PREFS_KEY, emptySet()) ?: emptySet()
        )
    }

    private fun saveFavoritesIds(recipesIdSet: Set<String>) {
        val sharedPreferences = application.applicationContext.getSharedPreferences(
            Constants.ASFOAPP_PREFS_FILE_KEY, Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putStringSet(Constants.FAVORITES_PREFS_KEY, recipesIdSet)
            apply()
        }
    }

    fun toggleFavoriteState() {
        val favoritesIds = getFavoritesIds()
        recipeState.value?.recipe?.id?.let { id ->
            val isFavorite = favoritesIds.contains(id.toString())
            val updatedFavoritesIds =
                if (isFavorite) favoritesIds - id.toString() else favoritesIds + id.toString()
            saveFavoritesIds(updatedFavoritesIds)
            _recipeState.value = recipeState.value?.copy(isFavorite = !isFavorite)
        }
    }

    fun setPortionsCount(progress: Int) {
        _recipeState.value = recipeState.value?.copy(portionsCount = progress)
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val apiHeaderImageUrl: String = "",
        val portionsCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}