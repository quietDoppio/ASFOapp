package com.example.asfoapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(tableName = "recipes")
data class Recipe(
    @SerialName("category_id")
    @ColumnInfo("category_id")
    val categoryId: Int? = null,
    @PrimaryKey
    @SerialName("id")
    @ColumnInfo("recipe_id")
    val recipeId: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    @ColumnInfo("image_url")
    val imageUrl: String,
    @ColumnInfo("is_favorite")
    val isFavorite: Boolean = false,
) : Parcelable