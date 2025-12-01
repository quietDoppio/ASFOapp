package com.example.asfoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @ColumnInfo("category_id")
    val categoryId: Int? = null,
    @PrimaryKey
    @ColumnInfo("recipe_id")
    val recipeId: Int,
    val title: String,
    val ingredients: List<IngredientEntityModel>,
    val method: List<String>,
    @ColumnInfo("image_url")
    val imagePath: String,
    @ColumnInfo("is_favorite")
    val isFavorite: Boolean = false,
)
