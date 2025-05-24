package com.example.asfoapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.repositories.CategoryRepository
import com.example.asfoapp.model.Category
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: CategoryRepository) : ViewModel() {

    private var _categoriesState: MutableLiveData<CategoriesState> =
        MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadCategories() {
        viewModelScope.launch {
            val cashedCategories = repository.getCategoriesFromCash()
            if (cashedCategories.isNotEmpty()) {
                _categoriesState.value =
                    categoriesState.value?.copy(categoriesList = cashedCategories)
                return@launch
            }

            val categories = repository.getCategories()
            if (categories.isNotEmpty()) {
                _categoriesState.value =
                    categoriesState.value?.copy(categoriesList = categories)
                return@launch
            }

            _toastMessage.postValue(Constants.NET_ERROR_MESSAGE)
        }
    }

    suspend fun getCategoryById(id: Int): Category? =
        repository.getCategoryByIdFromCash(id) ?: repository.getCategoryById(id)

    data class CategoriesState(
        val categoriesList: List<Category> = emptyList(),
    )
}