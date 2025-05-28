package com.example.asfoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val constructors: Map<Class<out ViewModel>, () -> ViewModel>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val modelProvider = constructors[modelClass]
            ?: constructors.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown viewModel - ${modelClass.name}")
        return modelProvider() as T
    }
}