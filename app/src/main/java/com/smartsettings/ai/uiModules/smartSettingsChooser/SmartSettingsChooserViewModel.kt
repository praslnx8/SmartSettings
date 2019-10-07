package com.smartsettings.ai.uiModules.smartSettingsChooser

import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.smartSettings.SmartSetting
import javax.inject.Inject

class SmartSettingsChooserViewModel : ViewModel() {

    init {
        SmartApp.appComponent.inject(this)
    }

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    fun addSmartSetting(smartSetting: SmartSetting<out Any>) {
        SmartProfile.addSmartSetting(smartSettingRepository, smartSetting)
    }
}