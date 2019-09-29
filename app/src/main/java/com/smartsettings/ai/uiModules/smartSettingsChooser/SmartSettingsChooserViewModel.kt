package com.smartsettings.ai.uiModules.smartSettingsChooser

import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.SmartProfile
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.repositories.SmartSettingRepository
import javax.inject.Inject

class SmartSettingsChooserViewModel : ViewModel() {

    init {
        SmartApp.appComponent.inject(this)
    }

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    fun addSmartSetting(smartSetting: SmartSetting<out Any, out Any, out Any>) {
        SmartProfile.enableSmartSetting(smartSettingRepository, smartSetting)
    }
}