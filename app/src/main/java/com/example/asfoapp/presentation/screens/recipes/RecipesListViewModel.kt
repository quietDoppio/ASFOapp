package com.example.asfoapp.presentation.screens.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.R
import com.example.asfoapp.core.LogConfig
import com.example.asfoapp.domain.repositories.RecipesRepository
import com.example.asfoapp.domain.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(private val repository: RecipesRepository) : ViewModel() {

    private val _recipesListState: MutableLiveData<RecipesListState> =
        MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState

    private var _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int> get() = _toastMessage

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
                            apiHeaderImageUrl = category.imageUrl
                        )
                    _recipesListState.value = recipesListState.value?.copy(recipes = remote.await())
                } catch (e: Exception) {
                    Log.d(LogConfig.LOG_TAG, "loadRecipes: ошибка загрузки данных")
                    _toastMessage.value = R.string.error_loading_data
                }
            }
        } else {
            Log.d(LogConfig.LOG_TAG, "loadRecipes: категория не обнаружена")
            _toastMessage.value = R.string.error_loading_data
        }
    }
}
