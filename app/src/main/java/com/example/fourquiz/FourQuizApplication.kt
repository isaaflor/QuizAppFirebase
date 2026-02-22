package com.example.fourquiz

import android.app.Application
import com.example.fourquiz.di.AppContainer

class FourQuizApplication : Application() {

    // The container is instantiated when the App starts up
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
