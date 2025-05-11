package com.example.asfoapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asfoapp.data.RecipeRepository
import com.example.asfoapp.model.Category

class CategoriesViewModel : ViewModel() {
    init {
        Log.i(TAG, "CategoriesViewModel is created")
    }
    private var _categoriesState: MutableLiveData<CategoriesState> = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState

    fun loadCategories(){
        _categoriesState.value = categoriesState.value?.copy(categoriesList = RecipeRepository.getCategories() ?: emptyList())
    }

    fun getCategoryById(categoryId: Int): Category? {
        return RecipeRepository.getCategoryById(categoryId)
    }

    data class CategoriesState(
        val categoriesList: List<Category> = emptyList(),
    )
}