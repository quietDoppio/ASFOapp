package com.example.asfoapp.data.database.entities

import kotlinx.serialization.Serializable

@Serializable
data class IngredientEntityModel(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
)
