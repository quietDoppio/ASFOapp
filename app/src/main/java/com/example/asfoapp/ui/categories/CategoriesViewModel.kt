package com.example.asfoapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asfoapp.data.STUB
import com.example.asfoapp.model.Category

class CategoriesViewModel : ViewModel() {
    private var _categoriesState: MutableLiveData<CategoriesState> = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState

    fun loadCategories(){
        _categoriesState.value = categoriesState.value?.copy(categoriesList = STUB.getCategories())
    }

    fun getCategoryById(categoryId: Int): Category? {
        return STUB.getCategoryById(categoryId)
    }

    data class CategoriesState(
        val categoriesList: List<Category> = emptyList(),
    )
}