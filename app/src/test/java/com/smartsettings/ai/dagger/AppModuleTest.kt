package com.smartsettings.ai.dagger

import android.content.Context
import android.media.AudioManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AppModuleTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var audioManager: AudioManager


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun provide_context_returns_context() {
        assertEquals(context, AppModule(context).provideContext())
    }

    @Test
    fun provide_audio_manager_returns_audio_manager() {

        Mockito.`when`(context.getSystemService(Context.AUDIO_SERVICE)).thenReturn(audioManager)

        assertEquals(audioManager, AppModule(context).provideAudioManager())
    }
}
