package com.smartsettings.ai.repositories

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.SmartProfile
import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.resources.db.SmartSettingDao
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

class SmartSettingRepository {

    @Inject
    lateinit var smartSettingDao: SmartSettingDao

    init {
        SmartApp.appComponent.inject(this)
    }

    val smartSettingsLiveData: MutableLiveData<List<Pair<SmartSetting, Boolean>>> = MutableLiveData()

    fun getSmartSettings() {
        val smartSettings = SmartProfile.getSmartSettings()
        if (smartSettings.isEmpty()) {

            doAsync {
                val smartSettingsFromDb = smartSettingDao.getSmartSettings()

                for (smartSettingDbData in smartSettingsFromDb) {
                    if (smartSettingDbData.type == "LOC") {
                        val locationBasedVolumeSetting =
                            Gson().fromJson(smartSettingDbData.serializedObject, LocationBasedVolumeSetting::class.java)
                        if (smartSettingDbData.enabled) {
                            SmartProfile.enableSmartSetting(locationBasedVolumeSetting)
                        } else {
                            SmartProfile.disableSmartSetting(locationBasedVolumeSetting)
                        }
                    }
                }

                uiThread {
                    smartSettingsLiveData.value = SmartProfile.getSmartSettings()
                }
            }

        } else {
            smartSettingsLiveData.value = smartSettings
        }
    }



}