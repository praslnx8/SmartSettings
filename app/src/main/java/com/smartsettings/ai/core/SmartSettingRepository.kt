package com.smartsettings.ai.core

import android.content.Context
import com.google.gson.Gson
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.core.smartSettings.LocationBasedSmartSetting
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.core.smartSettings.SmartSettingType
import com.smartsettings.ai.core.smartSettings.WifiBasedSmartSetting
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.resources.db.SettingChangerDBModel
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

    fun getSmartSettings(smartSettingsCallBack: (List<SmartSetting<out Any>>) -> Unit) {

        doAsync {

            val smartSettings = ArrayList<SmartSetting<out Any>>()

            val smartSettingsFromDb = smartSettingDao.getSmartSettings()

            for (smartSettingDbData in smartSettingsFromDb) {
                val settingChangers = createSettingChangersFromDbModels(smartSettingDbData.settingChangers)

                val smartSetting = if (smartSettingDbData.type == SmartSettingType.LOCATION_BASED_SETTING.value) {

                    val criteriaData =
                        Gson().fromJson(smartSettingDbData.serializedCriteriaData, LocationData::class.java)
                    val locationBasedVolumeSetting =
                        LocationBasedSmartSetting(smartSettingDbData.name, criteriaData)
                    locationBasedVolumeSetting.addSettingChangers(settingChangers)
                    locationBasedVolumeSetting
                } else if (smartSettingDbData.type == SmartSettingType.WIFI_BASED_SETTING.value) {

                    val wifiBasedSmartSetting =
                        WifiBasedSmartSetting(smartSettingDbData.name, smartSettingDbData.serializedCriteriaData)
                    wifiBasedSmartSetting.addSettingChangers(settingChangers)
                    wifiBasedSmartSetting
                } else {
                    throw IllegalArgumentException("Undefined setting type from db")
                }

                smartSettings.add(smartSetting)
            }

            uiThread {
                smartSettingsCallBack(smartSettings)
            }
        }
    }

    private fun createSettingChangersFromDbModels(settingChangerDbModels: List<SettingChangerDBModel>): Set<SettingChanger<out Any>> {
        val settingChangers = mutableSetOf<SettingChanger<out Any>>()

        settingChangerDbModels.forEach {
            if (it.type == SettingChangerType.VOLUME_CHANGER.value) {
                val volumeActionData = Gson().fromJson(it.serializedActionData, VolumeActionData::class.java)
                settingChangers.add(VolumeSettingChanger(volumeActionData))
            }
        }

        return settingChangers
    }

    fun addSmartSetting(smartSetting: SmartSetting<out Any>) {

        val smartSettingType = if (smartSetting is LocationBasedSmartSetting) {
            SmartSettingType.LOCATION_BASED_SETTING.value
        } else if (smartSetting is WifiBasedSmartSetting) {
            SmartSettingType.WIFI_BASED_SETTING.value
        } else {
            throw IllegalArgumentException("Type not added as functionality")
        }

        val smartSettingDBModel = SmartSettingDBModel(
            null,
            smartSettingType,
            smartSetting.name,
            smartSetting.criteriaData.serialize(),
            convertToSettingChangerDBList(smartSetting.getSettingChangers()),
            1
        )

        doAsync {
            smartSettingDao.insertSmartSetting(smartSettingDBModel)
        }
    }

    private fun convertToSettingChangerDBList(settingChangers: Set<SettingChanger<out Any>>): List<SettingChangerDBModel> {
        val settingChangerDbModels = arrayListOf<SettingChangerDBModel>()

        settingChangers.forEach {

            val settingChangerType = if (it is VolumeSettingChanger) {
                SettingChangerType.VOLUME_CHANGER.value
            } else {
                throw IllegalArgumentException("Type not added as functinality")
            }

            settingChangerDbModels.add(
                SettingChangerDBModel(
                    settingChangerType,
                    it.serializableActionData.serialize()
                )
            )
        }

        return settingChangerDbModels
    }

    fun updateSmartSetting(smartSetting: SmartSetting<out Any>) {

        val smartSettingType = if (smartSetting is LocationBasedSmartSetting) {
            SmartSettingType.LOCATION_BASED_SETTING.value
        } else if (smartSetting is WifiBasedSmartSetting) {
            SmartSettingType.WIFI_BASED_SETTING.value
        } else {
            throw IllegalArgumentException("Undefined type")
        }

        val smartSettingDBModelToUpdate = SmartSettingDBModel(
            null,
            smartSettingType,
            smartSetting.name,
            smartSetting.criteriaData.serialize(),
            convertToSettingChangerDBList(smartSetting.getSettingChangers()),
            1
        )

        doAsync {

            val smartSettingDbModelFromDb = smartSettingDao.getSmartSettingByName(smartSetting.name)
            if (smartSettingDbModelFromDb != null) {
                smartSettingDBModelToUpdate.id = smartSettingDbModelFromDb.id
                smartSettingDao.updateSmartSetting(smartSettingDBModelToUpdate)
            }
        }
    }

    fun deleteSmartSetting(smartSetting: SmartSetting<out Any>) {

        if (smartSetting is LocationBasedSmartSetting) {
            val smartSettingDBModelToUpdate = SmartSettingDBModel(
                null,
                SmartSettingType.LOCATION_BASED_SETTING.value,
                smartSetting.name,
                smartSetting.criteriaData.serialize(),
                convertToSettingChangerDBList(smartSetting.getSettingChangers()),
                1
            )

            doAsync {

                val smartSettingDbModelFromDb = smartSettingDao.getSmartSettingByName(smartSetting.name)
                if (smartSettingDbModelFromDb != null) {
                    smartSettingDBModelToUpdate.id = smartSettingDbModelFromDb.id
                    smartSettingDao.deleteSmartSetting(smartSettingDBModelToUpdate)
                }
            }
        }
    }
}