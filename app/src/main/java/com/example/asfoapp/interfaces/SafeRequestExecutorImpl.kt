package com.example.asfoapp.interfaces

import android.util.Log
import com.example.asfoapp.data.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SafeRequestExecutorImpl : SafeExecutor {
    override suspend fun <T> safeExecuteRequest(task: suspend () -> T): T? {
        val callerFunName = Throwable().stackTrace[1].methodName
        return withContext(Dispatchers.IO) {
            try {
                task()
            } catch (e: Exception) {
                Log.e(
                    Constants.LOG_TAG,
                    "$callerFunName - Ошибка загрузки данных, ${Log.getStackTraceString(e)}"
                )
                null
            }
        }
    }
}