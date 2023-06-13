package com.xabbok.nmediamaps

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NMediaMapsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //DependencyContainer.initApp(this)
    }



}
