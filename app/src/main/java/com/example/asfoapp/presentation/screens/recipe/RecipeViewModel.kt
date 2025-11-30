package com.example.asfoapp.presentation.screens.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.R
import com.example.asfoapp.domain.models.Recipe
import com.example.asfoapp.domain.repositories.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipesRepository) : ViewModel() {

    private val _recipeState: MutableLiveData<RecipeState> = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    private var _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int> get() = _toastMessage

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
                        apiHeaderImageUrl = cached.imageUrl,
                        portionsCount = recipeState.value?.portionsCount ?: 1,
                        isFavorite = cached.isFavorite,
                    )
                val remotedAwait = deferredRemotedRecipe.await()
                _recipeState.value =
                    recipeState.value?.copy(
                        recipe = remotedAwait,
                        apiHeaderImageUrl = remotedAwait.imageUrl,
                    )
            } catch (e: Exception) {
                _toastMessage.value = R.string.error_loading_data
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
}
