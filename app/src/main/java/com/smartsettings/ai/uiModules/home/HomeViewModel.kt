package com.smartsettings.ai.uiModules.home

import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.smartSettings.SmartSetting
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    init {
        SmartApp.appComponent.inject(this)
    }

    val smartSettingLiveData = SmartProfile.getSmartSettingLiveData()

    fun getSmartSettings() {
        SmartProfile.load(smartSettingRepository)
    }

    fun smartSettingChangedFromUser(smartSetting: SmartSetting<out Any>) {
        SmartProfile.updateSmartSetting(smartSettingRepository, smartSetting)
    }
}