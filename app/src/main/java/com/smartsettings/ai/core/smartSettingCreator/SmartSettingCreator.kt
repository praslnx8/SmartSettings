package com.smartsettings.ai.core.smartSettingCreator

import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.contextListeners.ContextListener
import com.smartsettings.ai.core.contextListeners.LocationContextListener
import com.smartsettings.ai.core.contextListeners.WifiContextListener
import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.di.DependencyProvider
import com.smartsettings.ai.resources.db.ContextListenerSchemaDBModel
import com.smartsettings.ai.resources.db.SettingChangerSchemaDBModel
import com.smartsettings.ai.resources.db.SmartSettingSchemaDBModel
import core.ContextListenerType
import core.SettingChangerType

class SmartSettingCreator {

    private val smartSettingSchemaRepo: SmartSettingSchemaRepo =
        DependencyProvider.smartSettingSchemaRepo

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

        parseContextListenersSchema(
            contextListenerSchemas,
            smartSettingCreatorCallback
        ) { contextListeners ->
            parseSettingChangersSchema(
                settingChangerSchemas,
                smartSettingCreatorCallback
            ) { settingChangers ->
                smartSettingCreatorCallback.getName { name ->
                    val smartSetting = SmartSetting(
                        null,
                        name ?: smartSettingSchemaDBModel.title,
                        contextListeners,
                        settingChangers,
                        smartSettingSchemaDBModel.conjunctionLogic
                    )
                    smartSetting.isShowNotificationOnTrigger = true
                    SmartProfile.addSmartSetting(smartSetting)
                    smartSettingCreatorCallback.onSmartSettingsCreated(smartSetting)
                }
            }
        }

    }

    private fun parseSettingChangersSchema(
        settingChangerSchemas: List<SettingChangerSchemaDBModel>,
        smartSettingCreatorCallback: SmartSettingCreatorCallback,
        settingChangerCallback: (Set<SettingChanger<out Any>>) -> Unit
    ) {

        val settingChangerTypes = ArrayList<Pair<SettingChangerType, String?>>()
        for (settingChangerSchema in settingChangerSchemas) {
            settingChangerTypes.add(Pair(settingChangerSchema.type, settingChangerSchema.input))
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
        contextListenerSchemas: List<ContextListenerSchemaDBModel>,
        smartSettingCreatorCallback: SmartSettingCreatorCallback,
        contextListenerCallback: (Set<ContextListener<out Any>>) -> Unit
    ) {
        val contextListenerTypes = ArrayList<Pair<ContextListenerType, String?>>()
        for (contextListenerSchema in contextListenerSchemas) {
            contextListenerTypes.add(Pair(contextListenerSchema.type, contextListenerSchema.input))
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
        contextListenerTypes: List<Pair<ContextListenerType, String?>>,
        criteriaDataCallback: (Map<ContextListenerType, Any>) -> Unit
    )

    fun getSettingChangerActionData(
        settingChangerTypes: List<Pair<SettingChangerType, String?>>,
        actionDataCallback: (Map<SettingChangerType, Any>) -> Unit
    )

    fun getName(nameCallback: (String?) -> Unit)

    fun onSmartSettingsCreated(smartSetting: SmartSetting)
}