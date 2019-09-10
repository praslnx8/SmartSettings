package com.smartsettings.ai.models

import com.smartsettings.ai.models.changedData.ChangedData
import com.smartsettings.ai.models.smartSettings.SmartSetting
import org.junit.Test

class SmartProfileTest {

    @Test
    fun enable_smart_setting_should_set_true_against_setting() {

        val smartSetting = object : SmartSetting {
            override fun applyChanges(currentState: SettingState): SettingState {
                return currentState
            }

            override fun askPermissions() {
            }

            override fun criteriaMatching(changedData: ChangedData): Boolean {
                return true
            }

        }

        SmartProfile.enableSmartSetting(smartSetting)
        assert(SmartProfile.getSmartSettings().contains(Pair(smartSetting, true)))
    }

    @Test
    fun disable_smart_setting_should_set_false_against_setting() {

        val smartSetting = object : SmartSetting {
            override fun applyChanges(currentState: SettingState): SettingState {
                return currentState
            }

            override fun askPermissions() {
            }

            override fun criteriaMatching(changedData: ChangedData): Boolean {
                return true
            }

        }

        SmartProfile.disableSmartSetting(smartSetting)
        assert(SmartProfile.getSmartSettings().contains(Pair(smartSetting, false)))
    }
}