package com.smartsettings.ai.repositories

import com.google.gson.Gson
import com.smartsettings.ai.SmartApp
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

    fun getSmartSettings(smartSettingsCallBack: (List<SmartSetting<Any>>) -> Unit) {

        doAsync {

            val smartSettings = ArrayList<SmartSetting<Any>>()

            val smartSettingsFromDb = smartSettingDao.getSmartSettings()

            for (smartSettingDbData in smartSettingsFromDb) {
                if (smartSettingDbData.type == "LOC") {
                    val locationBasedVolumeSetting =
                        Gson().fromJson(smartSettingDbData.serializedObject, LocationBasedVolumeSetting::class.java)

                    if (locationBasedVolumeSetting != null) {
                        smartSettings.add(locationBasedVolumeSetting)
                    }
                }
            }

            uiThread {
                smartSettingsCallBack(smartSettings)
            }
        }
    }
}