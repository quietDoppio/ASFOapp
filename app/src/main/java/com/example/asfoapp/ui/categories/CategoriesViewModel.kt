package com.example.asfoapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asfoapp.data.RecipeRepository
import com.example.asfoapp.model.Category
import com.example.asfoapp.ui.NET_ERROR_MESSAGE

class CategoriesViewModel : ViewModel() {

    private var _categoriesState: MutableLiveData<CategoriesState> =
        MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadCategories() {
        val safeStateCopy = categoriesState.value?.copy()
        RecipeRepository.getCategories { categories ->
            if (categories == null) {
                _toastMessage.postValue(NET_ERROR_MESSAGE)
            } else {
                _categoriesState.postValue(
                    safeStateCopy?.copy(
                        categoriesList = categories
                    )
                )
            }
        }
    }

    fun getCategoryById(id: Int, callback: (Category?) -> Unit) {
        RecipeRepository.getCategoryById(id) { category ->
            callback(category)
        }
    }

    data class CategoriesState(
        val categoriesList: List<Category> = emptyList(),
    )
}