package com.smartsettings.ai

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.smartsettings.ai.dagger.*

class SmartApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent

        @VisibleForTesting
        fun setDaggerComponentForTesting(context: Context) {
            appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(context))
                .daoModule(DaoModule(context))
                .repoModule(RepoModule())
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
            .daoModule(DaoModule(app))
            .repoModule(RepoModule())
            .build()
}