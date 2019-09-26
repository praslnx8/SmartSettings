package com.smartsettings.ai.models

import com.smartsettings.ai.models.smartSettings.SmartSetting
import org.junit.Test
import org.mockito.Mockito.mock

class SmartProfileTest {

    @Test
    fun enable_smart_setting_should_set_true_against_setting() {

        val smartSetting = mock(SmartSetting::class.java)

        SmartProfile.enableSmartSetting(smartSetting)
        assert(SmartProfile.getSmartSettings().contains(Pair(smartSetting, true)))
    }

    @Test
    fun disable_smart_setting_should_set_false_against_setting() {

        val smartSetting = mock(SmartSetting::class.java)

        SmartProfile.disableSmartSetting(smartSetting)
        assert(SmartProfile.getSmartSettings().contains(Pair(smartSetting, false)))
    }
}