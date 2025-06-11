package com.example.asfoapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asfoapp.data.Constants
import com.example.asfoapp.data.repositories.RecipesRepository
import com.example.asfoapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: RecipesRepository) :
    ViewModel() {

    private val _favoritesState: MutableLiveData<FavoritesState> = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favorites = repository.getFavoritesRecipes()
                _favoritesState.value =
                    favoritesState.value?.copy(favoritesRecipes = favorites)
            } catch (e: Exception) {
                _toastMessage.value = Constants.ERROR_MESSAGE
            }
        }
    }

    data class FavoritesState(
        val favoritesRecipes: List<Recipe> = emptyList()
    )
}