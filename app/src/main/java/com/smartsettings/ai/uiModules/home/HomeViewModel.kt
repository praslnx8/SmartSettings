package com.smartsettings.ai.uiModules.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.repositories.SmartSettingRepository
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    init {
        SmartApp.appComponent.inject(this)
    }

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    fun getSmartSettings(): LiveData<List<Pair<SmartSetting, Boolean>>> {

        return smartSettingRepository.getSmartSettings()
    }
}