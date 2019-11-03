package com.smartsettings.ai.core

import android.content.Context
import com.google.gson.Gson
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.contextListeners.ContextListener
import com.smartsettings.ai.core.contextListeners.ContextListenerType
import com.smartsettings.ai.core.contextListeners.LocationContextListener
import com.smartsettings.ai.core.contextListeners.WifiContextListener
import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.resources.db.ContextListenerDBModel
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

    fun getSmartSettings(smartSettingsCallBack: (List<SmartSetting>) -> Unit) {

        doAsync {

            val smartSettings = ArrayList<SmartSetting>()

            val smartSettingsFromDb = smartSettingDao.getSmartSettings()

            for (smartSettingDbData in smartSettingsFromDb) {
                val name = smartSettingDbData.name
                val settingChangers = createSettingChangersFromDbModels(smartSettingDbData.settingChangers)
                val contextListeners = createContextListenersFromDbModels(smartSettingDbData.contextListeners)
                val conjunctionLogic = smartSettingDbData.conjunctionLogic

                smartSettings.add(
                    SmartSetting(
                        smartSettingDbData.id,
                        name,
                        contextListeners,
                        settingChangers,
                        conjunctionLogic
                    )
                )
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

    private fun createContextListenersFromDbModels(contextListenersDbModels: List<ContextListenerDBModel>): Set<ContextListener<out Any>> {
        val contextListeners = mutableSetOf<ContextListener<out Any>>()

        contextListenersDbModels.forEach {
            if (it.type == ContextListenerType.LOCATION_LISTENER.value) {
                val locationCriteriaData = Gson().fromJson(it.serializedCriteriaData, LocationData::class.java)
                contextListeners.add(LocationContextListener(locationCriteriaData))
            } else if (it.type == ContextListenerType.WIFI_LISTENER.value) {
                val wifiCriteriaData = it.serializedCriteriaData
                contextListeners.add(WifiContextListener(wifiCriteriaData))
            }
        }

        return contextListeners
    }

    fun addSmartSetting(smartSetting: SmartSetting, addSmartSettingCallback: (Long) -> Unit) {

        val smartSettingDBModel = SmartSettingDBModel(
            null,
            smartSetting.name,
            convertToSettingChangerDBList(smartSetting.settingChangers),
            convertToContextListenerDBList(smartSetting.contextListeners),
            smartSetting.conjunctionLogic
        )

        doAsync {
            val id = smartSettingDao.insertSmartSetting(smartSettingDBModel)
            uiThread {
                addSmartSettingCallback(id)
            }
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

    private fun convertToContextListenerDBList(contextListeners: Set<ContextListener<out Any>>): List<ContextListenerDBModel> {
        val settingChangerDbModels = arrayListOf<ContextListenerDBModel>()

        contextListeners.forEach {

            val contextListenerType = if (it is LocationContextListener) {
                ContextListenerType.LOCATION_LISTENER.value
            } else {
                throw IllegalArgumentException("Type not added as functinality")
            }

            settingChangerDbModels.add(
                ContextListenerDBModel(
                    contextListenerType,
                    it.serializableCriteriaData.serialize()
                )
            )
        }

        return settingChangerDbModels
    }

    fun updateSmartSetting(smartSetting: SmartSetting, updateCallback: () -> Unit) {

        val smartSettingDBModelToUpdate = SmartSettingDBModel(
            null,
            smartSetting.name,
            convertToSettingChangerDBList(smartSetting.settingChangers),
            convertToContextListenerDBList(smartSetting.contextListeners),
            smartSetting.conjunctionLogic
        )

        doAsync {

            val smartSettingDbModelFromDb = smartSettingDao.getSmartSettingByName(smartSetting.name)
            if (smartSettingDbModelFromDb != null) {
                smartSettingDBModelToUpdate.id = smartSettingDbModelFromDb.id
                smartSettingDao.updateSmartSetting(smartSettingDBModelToUpdate)
            }

            uiThread {
                updateCallback()
            }
        }
    }

    fun deleteSmartSetting(smartSetting: SmartSetting, deleteCallback: () -> Unit) {

        val smartSettingDBModelToUpdate = SmartSettingDBModel(
            null,
            smartSetting.name,
            convertToSettingChangerDBList(smartSetting.settingChangers),
            convertToContextListenerDBList(smartSetting.contextListeners),
            smartSetting.conjunctionLogic
        )

        doAsync {

            val smartSettingDbModelFromDb = smartSettingDao.getSmartSettingByName(smartSetting.name)
            if (smartSettingDbModelFromDb != null) {
                smartSettingDBModelToUpdate.id = smartSettingDbModelFromDb.id
                smartSettingDao.deleteSmartSetting(smartSettingDBModelToUpdate)
            }

            uiThread {
                deleteCallback()
            }
        }

    }
}