package com.example.asfoapp.data.mappers

import com.example.asfoapp.data.api.NetworkConfig
import com.example.asfoapp.data.api.model.IngredientDto
import com.example.asfoapp.data.api.model.RecipeDto
import com.example.asfoapp.data.database.entities.IngredientEntityModel
import com.example.asfoapp.data.database.entities.RecipeEntity
import com.example.asfoapp.domain.models.Ingredient
import com.example.asfoapp.domain.models.Recipe

fun IngredientDto.toDomain(): Ingredient =
    Ingredient(
        quantity = quantity,
        unitOfMeasure = unitOfMeasure,
        description = description,
    )

fun IngredientDto.toEntity(): IngredientEntityModel =
    IngredientEntityModel(
        quantity = quantity,
        unitOfMeasure = unitOfMeasure,
        description = description,
    )

fun IngredientEntityModel.toDomain(): Ingredient =
    Ingredient(
        quantity = quantity,
        unitOfMeasure = unitOfMeasure,
        description = description,
    )

fun RecipeDto.toDomain(
    isFavorite: Boolean,
    categoryIdOverride: Int? = categoryId,
): Recipe =
    Recipe(
        categoryId = categoryIdOverride ?: categoryId,
        recipeId = id,
        title = title,
        ingredients = ingredients.map { it.toDomain() },
        method = method,
        imageUrl = NetworkConfig.buildImageUrl(imageUrl),
        isFavorite = isFavorite,
    )

fun RecipeDto.toEntity(
    isFavorite: Boolean,
    categoryIdOverride: Int? = categoryId,
): RecipeEntity =
    RecipeEntity(
        categoryId = categoryIdOverride ?: categoryId,
        recipeId = id,
        title = title,
        ingredients = ingredients.map { it.toEntity() },
        method = method,
        imagePath = imageUrl,
        isFavorite = isFavorite,
    )

fun RecipeEntity.toDomain(): Recipe =
    Recipe(
        categoryId = categoryId,
        recipeId = recipeId,
        title = title,
        ingredients = ingredients.map { it.toDomain() },
        method = method,
        imageUrl = NetworkConfig.buildImageUrl(imagePath),
        isFavorite = isFavorite,
    )
