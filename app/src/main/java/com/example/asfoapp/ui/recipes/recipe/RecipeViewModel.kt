package com.example.asfoapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asfoapp.data.RecipeRepository
import com.example.asfoapp.model.Recipe

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _recipeState: MutableLiveData<RecipeState> = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    init {
        Log.i("!!!", "RecipeViewModel: init. recipeState changed")
    }

    fun loadRecipe(recipeId: Int) {
        RecipeRepository.getRecipeById(recipeId) { recipe ->
            val isFavorite = getFavoritesIds().contains(recipeId.toString())
            val drawable: Drawable? = getDrawableFromAssets(recipe?.imageUrl ?: "")

            _recipeState.value = recipe?.let {
                recipeState.value?.copy(
                    recipe = it,
                    recipeImage = drawable,
                    portionsCount = recipeState.value?.portionsCount ?: 1,
                    isFavorite = isFavorite,
                )
            }
        }

    }

    private fun getFavoritesIds(): MutableSet<String> {
        val sharedPreferences = application.applicationContext.getSharedPreferences(
            ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        return HashSet(
            sharedPreferences?.getStringSet(FAVORITES_PREFS_KEY, emptySet()) ?: emptySet()
        )
    }

    private fun saveFavoritesIds(recipesIdSet: Set<String>) {
        val sharedPreferences = application.applicationContext.getSharedPreferences(
            ASFOAPP_PREFS_FILE_KEY,
            Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putStringSet(FAVORITES_PREFS_KEY, recipesIdSet)
            apply()
        }
    }

    private fun getDrawableFromAssets(imageUrl: String): Drawable? {
        return try {
            val inputStream = application.applicationContext.assets.open(imageUrl)
            Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            val stackTrace = Log.getStackTraceString(e)
            Log.e(
                "!!!",
                "Image - $imageUrl not found in assets\n$stackTrace"
            )
            null
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
        val recipeImage: Drawable? = null,
        val portionsCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}