package com.example.asfoapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.Constants
import com.example.asfoapp.data.RecipeRepository
import com.example.asfoapp.model.Category
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    private var _categoriesState: MutableLiveData<CategoriesState> =
        MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadCategories() {
        viewModelScope.launch {
            val categories = RecipeRepository.getCategories()
            if (categories == null) {
                _toastMessage.postValue(Constants.NET_ERROR_MESSAGE)
            } else {
                _categoriesState.postValue(
                    categoriesState.value?.copy(categoriesList = categories)
                )
            }
        }
    }

    suspend fun getCategoryById(id: Int) = RecipeRepository.getCategoryById(id)

    data class CategoriesState(
        val categoriesList: List<Category> = emptyList(),
    )
}