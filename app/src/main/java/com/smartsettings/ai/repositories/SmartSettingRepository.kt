package com.smartsettings.ai.repositories

import android.content.Context
import com.google.gson.Gson
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.models.actionData.VolumeActionData
import com.smartsettings.ai.models.criteriaData.LocationData
import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.models.smartSettings.SmartSetting
import com.smartsettings.ai.resources.db.SmartSettingDBModel
import com.smartsettings.ai.resources.db.SmartSettingDao
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


class SmartSettingRepository {

    @Inject
    lateinit var smartSettingDao: SmartSettingDao

    @Inject
    lateinit var context: Context

    init {
        SmartApp.appComponent.inject(this)
    }

    fun getSmartSettings(smartSettingsCallBack: (List<SmartSetting<out Any, out Any, out Any>>) -> Unit) {

        doAsync {

            val smartSettings = ArrayList<SmartSetting<out Any, out Any, out Any>>()

            val smartSettingsFromDb = smartSettingDao.getSmartSettings()

            for (smartSettingDbData in smartSettingsFromDb) {
                if (smartSettingDbData.id != null) {
                    if (smartSettingDbData.type == "LOC") {

                        val criteriaData =
                            Gson().fromJson(smartSettingDbData.serializedCriteriaData, LocationData::class.java)
                        val actionData =
                            Gson().fromJson(smartSettingDbData.serializedActionData, VolumeActionData::class.java)

                        val locationBasedVolumeSetting = LocationBasedVolumeSetting(criteriaData, actionData)

                        smartSettings.add(locationBasedVolumeSetting)
                    }
                }
            }

            uiThread {
                smartSettingsCallBack(smartSettings)
            }
        }
    }

    fun addSmartSetting(smartSetting: SmartSetting<out Any, out Any, out Any>) {

        if (smartSetting is LocationBasedVolumeSetting) {
            val smartSettingDBModel = SmartSettingDBModel(
                1,
                "LOC",
                smartSetting.criteriaData.serialize(),
                smartSetting.actionData.serialize(),
                1
            )

            doAsync {
                smartSettingDao.insertSmartSetting(smartSettingDBModel)
            }
        }
    }
}