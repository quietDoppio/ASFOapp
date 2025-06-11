package com.example.asfoapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.repositories.RecipesRepository
import com.example.asfoapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipesRepository) : ViewModel() {

    private val _recipeState: MutableLiveData<RecipeState> = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            try {
                val deferredCachedRecipe: Deferred<Recipe> =
                    async { repository.getCachedRecipe(recipeId) }
                val deferredRemotedRecipe: Deferred<Recipe> =
                    async { repository.getRecipe(recipeId) }

                val cached = deferredCachedRecipe.await()
                _recipeState.value =
                    recipeState.value?.copy(
                        recipe = cached,
                        apiHeaderImageUrl = "${Constants.API_BASE_URL}images/${cached.imageUrl}",
                        portionsCount = recipeState.value?.portionsCount ?: 1,
                        isFavorite = cached.isFavorite,
                    )
                val remotedAwait = deferredRemotedRecipe.await()
                _recipeState.value =
                    recipeState.value?.copy(
                        recipe = remotedAwait,
                        apiHeaderImageUrl = "${Constants.API_BASE_URL}images/${remotedAwait.imageUrl}",
                    )
            } catch (e: Exception) {
                _toastMessage.value = Constants.ERROR_MESSAGE
            }
        }
    }

    fun toggleFavoriteState() {
        viewModelScope.launch {
            recipeState.value?.recipe?.recipeId?.let { id ->
                val isFavorite = repository.isRecipeFavorite(id)
                repository.setFavoriteState(id, !isFavorite)
                _recipeState.value = recipeState.value?.copy(isFavorite = !isFavorite)
            }
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