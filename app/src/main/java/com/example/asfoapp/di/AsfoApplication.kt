package com.example.asfoapp.di

import android.app.Application

class AsfoApplication: Application() {
    val container by lazy { AppContainer(this) }
}