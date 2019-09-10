package com.smartsettings.ai.modules.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.repositories.SmartSettingRepository
import com.smartsettings.ai.resources.db.SmartSettingDBModel
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    init {
        SmartApp.appComponent.inject(this)
    }

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    fun getSmartSettings(): LiveData<List<SmartSettingDBModel>> {
        return smartSettingRepository.smartSettings
    }
}