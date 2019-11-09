package com.smartsettings.ai

import android.app.Application
import com.smartsettings.ai.di.DependencyInjector
import com.smartsettings.ai.di.DependencyProvider

class SmartApp : Application() {

    override fun onCreate() {
        super.onCreate()

        DependencyProvider.setInjector(DependencyInjector(this))
    }
}

