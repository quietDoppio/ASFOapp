package com.example.asfoapp.presentation.di

import com.example.asfoapp.presentation.GlideRequestListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    @Singleton
    fun provideGlideRequestListener(): GlideRequestListener = GlideRequestListener()
}
