package com.example.asfoapp.data.mappers

import com.example.asfoapp.data.api.NetworkConfig
import com.example.asfoapp.data.api.model.CategoryDto
import com.example.asfoapp.data.database.entities.CategoryEntity
import com.example.asfoapp.domain.models.Category

fun CategoryDto.toEntity(): CategoryEntity =
    CategoryEntity(
        id = id,
        title = title,
        description = description,
        imagePath = imageUrl,
    )

fun CategoryDto.toDomain(): Category =
    Category(
        id = id,
        title = title,
        description = description,
        imageUrl = NetworkConfig.buildImageUrl(imageUrl),
    )

fun CategoryEntity.toDomain(): Category =
    Category(
        id = id,
        title = title,
        description = description,
        imageUrl = NetworkConfig.buildImageUrl(imagePath),
    )
