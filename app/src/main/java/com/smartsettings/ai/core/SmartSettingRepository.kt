package com.smartsettings.ai.core

import android.content.Context
import com.google.gson.Gson
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.core.smartSettings.LocationBasedSmartSetting
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.resources.db.SettingChangerDBModel
import com.smartsettings.ai.resources.db.SmartSettingDBModel
import com.smartsettings.ai.resources.db.SmartSettingDao
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


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
                if (smartSettingDbData.id != null) {
                    if (smartSettingDbData.type == SmartSettingType.LOCBASEDAUDIO.value) {

                        val criteriaData =
                            Gson().fromJson(smartSettingDbData.serializedCriteriaData, LocationData::class.java)
                        val settingChangers = createSettingChangersFromDbModels(smartSettingDbData.settingChangers)

                        val locationBasedVolumeSetting =
                            LocationBasedSmartSetting(smartSettingDbData.name, criteriaData)
                        locationBasedVolumeSetting.settingChangers.addAll(settingChangers)

                        smartSettings.add(locationBasedVolumeSetting)
                    }
                }
            }

            uiThread {
                smartSettingsCallBack(smartSettings)
            }
        }
    }

    private fun createSettingChangersFromDbModels(settingChangerDbModels: List<SettingChangerDBModel>): Set<SettingChanger<Any>> {
        val settingChangers = mutableSetOf<SettingChanger<Any>>()

        settingChangerDbModels.forEach {
            if (it.type == SettingChangetType.VOLUMECHANGER.value) {
                val volumeActionData = Gson().fromJson(it.serializedActionData, VolumeActionData::class.java)
                settingChangers.add(VolumeSettingChanger(volumeActionData))
            }
        }

        return settingChangers
    }

    fun addSmartSetting(smartSetting: SmartSetting<out Any>) {

        if (smartSetting is LocationBasedSmartSetting) {
            val smartSettingDBModel = SmartSettingDBModel(
                1,
                SmartSettingType.LOCBASEDAUDIO.value,
                smartSetting.name,
                smartSetting.criteriaData.serialize(),
                convertToSettingChangerDBList(smartSetting.settingChangers),
                1
            )

            doAsync {
                smartSettingDao.insertSmartSetting(smartSettingDBModel)
            }
        }
    }

    private fun convertToSettingChangerDBList(settingChangers: HashSet<SettingChanger<out Any>>): List<SettingChangerDBModel> {
        val settingChangerDbModels = arrayListOf<SettingChangerDBModel>()

        settingChangers.forEach {
            if (it is VolumeSettingChanger) {
                settingChangerDbModels.add(
                    SettingChangerDBModel(
                        SettingChangetType.VOLUMECHANGER.value,
                        it.serializableActionData.serialize()
                    )
                )
            }
        }

        return settingChangerDbModels
    }

    fun updateSmartSetting(smartSetting: SmartSetting<out Any>) {

        if (smartSetting is LocationBasedSmartSetting) {
            val smartSettingDBModelToUpdate = SmartSettingDBModel(
                null,
                SmartSettingType.LOCBASEDAUDIO.value,
                smartSetting.name,
                smartSetting.criteriaData.serialize(),
                convertToSettingChangerDBList(smartSetting.settingChangers),
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
    }

    fun deleteSmartSetting(smartSetting: SmartSetting<out Any>) {

        if (smartSetting is LocationBasedSmartSetting) {
            val smartSettingDBModelToUpdate = SmartSettingDBModel(
                null,
                SmartSettingType.LOCBASEDAUDIO.value,
                smartSetting.name,
                smartSetting.criteriaData.serialize(),
                convertToSettingChangerDBList(smartSetting.settingChangers),
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

enum class SmartSettingType(val value: String) {
    LOCBASEDAUDIO("locbasedaudio")
}

enum class SettingChangetType(val value: String) {
    VOLUMECHANGER("volumechanger")
}