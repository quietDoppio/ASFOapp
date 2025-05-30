package com.example.asfoapp.ui.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.repositories.RecipesRepository
import com.example.asfoapp.model.Category
import com.example.asfoapp.model.Recipe
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RecipesListViewModel(private val repository: RecipesRepository) : ViewModel() {

    private val _recipesListState: MutableLiveData<RecipesListState> =
        MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadRecipes(category: Category?) {
        if (category != null) {
            viewModelScope.launch {
                try {
                    val cached = async { repository.getCachedRecipesByCategoryId(category.id) }
                    val remote = async { repository.getRecipesByCategoryId(category.id) }
                    _recipesListState.value =
                        recipesListState.value?.copy(
                            categoryTitle = category.title,
                            recipes = cached.await(),
                            apiHeaderImageUrl = "${Constants.API_BASE_URL}images/${category.imageUrl}"
                        )
                    _recipesListState.value = recipesListState.value?.copy(recipes = remote.await())
                } catch (e: Exception) {
                    Log.d(Constants.LOG_TAG, "loadRecipes: ошибка загрузки данных")
                    _toastMessage.value = Constants.ERROR_MESSAGE
                }
            }
        } else {
            Log.d(Constants.LOG_TAG, "loadRecipes: категория не обнаружена")
            _toastMessage.value = Constants.ERROR_MESSAGE
        }
    }

    data class RecipesListState(
        val categoryTitle: String = "",
        val recipes: List<Recipe> = emptyList(),
        val apiHeaderImageUrl: String = "",
    )
}