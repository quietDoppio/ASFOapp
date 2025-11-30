package com.example.asfoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    @ColumnInfo(name = "image_url")
    val imagePath: String,
)
