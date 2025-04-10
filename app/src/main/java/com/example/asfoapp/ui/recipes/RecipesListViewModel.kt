package com.example.asfoapp.ui.recipes

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.asfoapp.data.STUB
import com.example.asfoapp.model.Category
import com.example.asfoapp.model.Recipe

class RecipesListViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _recipesListState: MutableLiveData<RecipesListState> = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState

    fun loadRecipes(category: Category) {
        val recipes = STUB.getRecipesByCategoryId(category.id)
        val imageDrawable = getDrawableFromAssets(category.imageUrl)

        _recipesListState.value = recipesListState.value?.copy(
            recipes = recipes,
            imageDrawable = imageDrawable
        )
    }
    private fun getDrawableFromAssets(imageUrl: String): Drawable? {
        return try {
            val inputStream = application.applicationContext.assets.open(imageUrl)
            Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            val stackTrace = Log.getStackTraceString(e)
            Log.e(
                "RecipesListFragment",
                "Image - $imageUrl not found in assets\n$stackTrace"
            )
            null
        }
    }

    data class RecipesListState(
        val recipes: List<Recipe> = emptyList(),
        val imageDrawable: Drawable? = null,
    )
}