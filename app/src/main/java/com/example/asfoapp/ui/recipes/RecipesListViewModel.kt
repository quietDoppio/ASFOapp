package com.example.asfoapp.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asfoapp.Constants
import com.example.asfoapp.data.RecipeRepository
import com.example.asfoapp.model.Category
import com.example.asfoapp.model.Recipe

class RecipesListViewModel: ViewModel() {

    private val _recipesListState: MutableLiveData<RecipesListState> =
        MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadRecipes(category: Category) {
        RecipeRepository.getRecipesByCategoryId(category.id) { recipes ->
            if (recipes == null) {
                _toastMessage.postValue(Constants.NET_ERROR_MESSAGE)
            } else {
                _recipesListState.postValue(
                    recipesListState.value?.copy(
                        recipes = recipes,
                        apiHeaderImageUrl = "${Constants.BASE_URL}images/${category.imageUrl}"
                        )
                )
            }
        }
    }

    data class RecipesListState(
        val recipes: List<Recipe> = emptyList(),
        val apiHeaderImageUrl: String = "",
    )
}