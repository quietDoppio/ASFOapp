package com.example.asfoapp.data.database

import androidx.room.TypeConverter
import com.example.asfoapp.model.Ingredient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromIngredientList(list: List<Ingredient>): String =
        json.encodeToString(ListSerializer(Ingredient.serializer()), list)

    @TypeConverter
    fun toIngredientList(data: String): List<Ingredient> =
        json.decodeFromString(ListSerializer(Ingredient.serializer()), data)

    @TypeConverter
    fun fromStringList(list: List<String>): String =
        list.joinToString("|")

    @TypeConverter
    fun toStringList(data: String): List<String> =
        if (data.isEmpty()) emptyList() else data.split("|")
}