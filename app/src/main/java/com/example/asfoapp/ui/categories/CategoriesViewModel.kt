package com.example.asfoapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.repositories.CategoryRepository
import com.example.asfoapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repository: CategoryRepository) : ViewModel() {

    private var _categoriesState: MutableLiveData<CategoriesState> =
        MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> get() = _categoriesState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val cached = async { repository.getCachedCategories() }
                val remote = async { repository.getCategories() }
                _categoriesState.value =
                    categoriesState.value?.copy(categoriesList = cached.await())
                _categoriesState.value =
                    categoriesState.value?.copy(categoriesList = remote.await())
            } catch (e: Exception) {
                Log.e(
                    Constants.LOG_TAG,
                    "loadCategories: ошибка загрузки данных. ${Log.getStackTraceString(e)}"
                )
                _toastMessage.value = Constants.ERROR_MESSAGE
            }
        }
    }

    suspend fun getCategoryById(id: Int): Category? {
        return try {
            try {
                repository.getCachedCategoryById(id)
            } catch (e: Exception) {
                repository.getCategoryById(id)
            }
        } catch (e: Exception) {
            Log.d(
                Constants.LOG_TAG,
                "getCategoryById: ошибка получения данных, ${Log.getStackTraceString(e)}"
            )
            null
        }
    }

    data class CategoriesState(
        val categoriesList: List<Category> = emptyList(),
    )
}