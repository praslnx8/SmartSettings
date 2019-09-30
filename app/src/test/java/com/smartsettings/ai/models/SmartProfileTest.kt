package com.smartsettings.ai.models

import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.repositories.SmartSettingRepository
import org.junit.Test
import org.mockito.Mockito.mock

class SmartProfileTest {

    private val smartSettingRepository: SmartSettingRepository = mock(SmartSettingRepository::class.java)

    @Test
    fun enable_smart_setting_should_set_true_against_setting() {

        val smartSetting = mock(LocationBasedVolumeSetting::class.java)

        SmartProfile.addSmartSetting(smartSettingRepository, smartSetting)
        assert((SmartProfile.getSmartSettingLiveData().value ?: HashSet()).contains(smartSetting))
    }

    @Test
    fun disable_smart_setting_should_set_false_against_setting() {

        val smartSetting = mock(LocationBasedVolumeSetting::class.java)

        SmartProfile.addSmartSetting(smartSettingRepository, smartSetting)
        assert((SmartProfile.getSmartSettingLiveData().value ?: HashSet()).contains(smartSetting))
        assert((SmartProfile.getSmartSettingLiveData().value ?: HashSet()).iterator().next().isEnabled())
    }
}