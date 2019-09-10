package com.smartsettings.ai.dagger

import android.content.Context
import android.media.AudioManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideAudioManager(): AudioManager = app.getSystemService(Context.AUDIO_SERVICE) as AudioManager
}