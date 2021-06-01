package com.example.shopy

import android.app.Application

object AppContextUtility : Application() {
    fun getAppContext() = applicationContext
}