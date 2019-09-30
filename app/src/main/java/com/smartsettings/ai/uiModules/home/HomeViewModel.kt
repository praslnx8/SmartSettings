package com.smartsettings.ai.uiModules.home

import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.SmartProfile
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.repositories.SmartSettingRepository
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

    fun smartSettingChangedFromUser(smartSetting: SmartSetting<out Any, out Any, out Any>) {
        SmartProfile.updateSmartSetting(smartSettingRepository, smartSetting)
    }
}