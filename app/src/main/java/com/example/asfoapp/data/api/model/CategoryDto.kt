package com.example.asfoapp.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
)
