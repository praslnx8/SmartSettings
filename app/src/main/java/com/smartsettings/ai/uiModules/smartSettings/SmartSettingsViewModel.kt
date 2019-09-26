package com.smartsettings.ai.uiModules.smartSettings

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.SmartProfile
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.repositories.SmartSettingRepository
import com.smartsettings.ai.resources.db.SmartSettingDBModel
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class SmartSettingsViewModel : ViewModel() {

    init {
        SmartApp.appComponent.inject(this)
    }

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    fun addSmartSetting(smartSetting: SmartSetting) {
        SmartProfile.enableSmartSetting(smartSetting)

        val smartSettingDBModel = SmartSettingDBModel(1, "loc", true, Gson().toJson(smartSetting), 1)

        doAsync {
            smartSettingRepository.smartSettingDao.insertSmartSetting(smartSettingDBModel)
        }
    }
}