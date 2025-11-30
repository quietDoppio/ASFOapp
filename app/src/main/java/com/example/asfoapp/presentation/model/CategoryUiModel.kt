package com.example.asfoapp.presentation.model

import android.os.Parcelable
import com.example.asfoapp.domain.models.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
) : Parcelable

fun Category.toUiModel(): CategoryUiModel =
    CategoryUiModel(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
    )

fun CategoryUiModel.toDomain(): Category =
    Category(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
    )
