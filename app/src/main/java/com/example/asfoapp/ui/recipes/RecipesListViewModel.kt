package com.example.asfoapp.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.repositories.CommonRepository
import com.example.asfoapp.model.Category
import com.example.asfoapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel : ViewModel() {

    private val _recipesListState: MutableLiveData<RecipesListState> =
        MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadRecipes(category: Category?) {
        if (category != null) {
            viewModelScope.launch {
                val recipes = CommonRepository.getRecipesByCategoryId(category.id)
                if (recipes == null) {
                    _toastMessage.postValue(Constants.NET_ERROR_MESSAGE)
                } else {
                    _recipesListState.postValue(
                        recipesListState.value?.copy(
                            categoryTitle = category.title,
                            recipes = recipes,
                            apiHeaderImageUrl = "${Constants.BASE_URL}images/${category.imageUrl}"
                        )
                    )
                }
            }
        } else {
            _toastMessage.postValue(Constants.NET_ERROR_MESSAGE)
        }
    }

    data class RecipesListState(
        val categoryTitle: String = "",
        val recipes: List<Recipe> = emptyList(),
        val apiHeaderImageUrl: String = "",
    )
}