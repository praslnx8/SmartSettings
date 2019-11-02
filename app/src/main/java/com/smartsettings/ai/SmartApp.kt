package com.smartsettings.ai

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.smartsettings.ai.di.AppComponent
import com.smartsettings.ai.di.AppModule
import com.smartsettings.ai.di.DaggerAppComponent

class SmartApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent

        @VisibleForTesting
        fun setDaggerComponentForTesting(appModule: AppModule) {
            appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }

    private fun initDagger(app: SmartApp): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}