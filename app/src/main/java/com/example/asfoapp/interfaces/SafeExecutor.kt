package com.example.asfoapp.interfaces

interface SafeExecutor {
    suspend fun <T> safeExecuteRequest(task: suspend () -> T): T?
}