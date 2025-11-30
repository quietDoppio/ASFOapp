package com.example.asfoapp.data.api

object NetworkConfig {
    const val API_BASE_URL = "https://recipes.androidsprint.ru/api/"
    private const val IMAGES_PATH = "images/"

    fun buildImageUrl(imagePath: String): String = "$API_BASE_URL$IMAGES_PATH$imagePath"
}
