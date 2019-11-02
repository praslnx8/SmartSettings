package com.smartsettings.ai.core.smartSettingCreator

import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
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
import com.smartsettings.ai.resources.db.SmartSettingSchemaDBModel
import javax.inject.Inject

class SmartSettingCreator {

    init {
        SmartApp.appComponent.inject(this)
    }

    @Inject
    lateinit var smartSettingSchemaRepo: SmartSettingSchemaRepo

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    fun getSmartSettingSchemas(schemasCallback: (List<SmartSettingSchemaDBModel>) -> Unit) {
        smartSettingSchemaRepo.getSchemas {
            schemasCallback(it)
        }
    }

    fun parseSmartSettingSchema(
        smartSettingSchemaDBModel: SmartSettingSchemaDBModel,
        smartSettingCreatorCallback: SmartSettingCreatorCallback
    ) {

        val contextListenerSchemas = smartSettingSchemaDBModel.contextListenerSchemas
        val settingChangerSchemas = smartSettingSchemaDBModel.settingChangerSchemas

        parseContextListenersSchema(contextListenerSchemas, smartSettingCreatorCallback) { contextListeners ->
            parseSettingChangersSchema(settingChangerSchemas, smartSettingCreatorCallback) { settingChangers ->
                smartSettingCreatorCallback.getName { name ->
                    val smartSetting = SmartSetting(
                        name,
                        contextListeners,
                        settingChangers,
                        smartSettingSchemaDBModel.conjunctionLogic
                    )

                    SmartProfile.addSmartSetting(smartSettingRepository, smartSetting)
                    smartSettingCreatorCallback.onSmartSettingsCreated(smartSetting)
                }
            }
        }

    }

    private fun parseSettingChangersSchema(
        settingChangerSchemas: List<String>,
        smartSettingCreatorCallback: SmartSettingCreatorCallback,
        settingChangerCallback: (Set<SettingChanger<out Any>>) -> Unit
    ) {

        val settingChangerTypes = ArrayList<SettingChangerType>()
        for (settingChangerSchema in settingChangerSchemas) {
            settingChangerTypes.add(SettingChangerType.valueOf(settingChangerSchema))
        }

        smartSettingCreatorCallback.getSettingChangerActionData(settingChangerTypes) {
            val settingChangers = HashSet<SettingChanger<out Any>>()
            for ((settingChangerType, actionData) in it) {
                if (settingChangerType == SettingChangerType.VOLUME_CHANGER) {
                    settingChangers.add(VolumeSettingChanger(actionData as VolumeActionData))
                }
            }

            settingChangerCallback(settingChangers)
        }
    }

    private fun parseContextListenersSchema(
        contextListenerSchemas: List<String>,
        smartSettingCreatorCallback: SmartSettingCreatorCallback,
        contextListenerCallback: (Set<ContextListener<out Any>>) -> Unit
    ) {
        val contextListenerTypes = ArrayList<ContextListenerType>()
        for (contextListenerSchema in contextListenerSchemas) {
            contextListenerTypes.add(ContextListenerType.valueOf(contextListenerSchema))
        }

        smartSettingCreatorCallback.getContextListenerCriteriaData(contextListenerTypes) {

            val contextListeners = HashSet<ContextListener<out Any>>()

            for ((contextListenerType, criteriaData) in it) {
                if (contextListenerType == ContextListenerType.LOCATION_LISTENER) {
                    contextListeners.add(LocationContextListener(criteriaData as LocationData))
                } else if (contextListenerType == ContextListenerType.WIFI_LISTENER) {
                    contextListeners.add(WifiContextListener(criteriaData as String))
                }
            }

            contextListenerCallback(contextListeners)
        }
    }
}

interface SmartSettingCreatorCallback {

    fun getContextListenerCriteriaData(
        contextListenerTypes: List<ContextListenerType>,
        criteriaDataCallback: (Map<ContextListenerType, Any>) -> Unit
    )

    fun getSettingChangerActionData(
        settingChangerTypes: List<SettingChangerType>,
        actionDataCallback: (Map<SettingChangerType, Any>) -> Unit
    )

    fun getName(nameCallback: (String) -> Unit)

    fun onSmartSettingsCreated(smartSetting: SmartSetting)
}