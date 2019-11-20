package com.smartsettings.ai.uiModules.smartSettingCreatorView

import androidx.lifecycle.ViewModel
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingCreator
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingCreatorCallback
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.di.DependencyProvider
import com.smartsettings.ai.resources.db.ContextListenerSchemaDBModel
import com.smartsettings.ai.resources.db.SettingChangerSchemaDBModel
import com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView.SmartSettingInputView
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.ContextListenerSchemaViewData
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.SettingChangerSchemaViewData
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.SmartSettingSchemaViewData

class SmartSettingCreatorViewModel : ViewModel(), SmartSettingCreatorPresenter {

    private lateinit var smartSettingShemaViewData: SmartSettingSchemaViewData

    private lateinit var smartSettingCreatorView: SmartSettingCreatorView

    private val smartSettingCreator : SmartSettingCreator = DependencyProvider.smartSettingCreator

    private val contextListenerInputViews =
        mutableMapOf<ContextListenerSchemaViewData, SmartSettingInputView<out Any>>()
    private val settingChangerInputViews =
        mutableMapOf<SettingChangerSchemaViewData, SmartSettingInputView<out Any>>()

    override fun setView(smartSettingCreatorView: SmartSettingCreatorView) {
        this.smartSettingCreatorView = smartSettingCreatorView
    }

    override fun setSchema(smartSettingShemaViewData: SmartSettingSchemaViewData) {
        this.smartSettingShemaViewData = smartSettingShemaViewData
        smartSettingCreatorView.setSmartSettingData(
            smartSettingShemaViewData.name,
            smartSettingShemaViewData.description
        )

        for(contextListenerSchemaViewData in smartSettingShemaViewData.contextListenerSchemas) {
            contextListenerInputViews[contextListenerSchemaViewData] = smartSettingCreatorView.addInputView(contextListenerSchemaViewData)
        }

        for(settingChangerSchemaViewData in smartSettingShemaViewData.settingChangerSchemas) {
            settingChangerInputViews[settingChangerSchemaViewData] = smartSettingCreatorView.addInputView(settingChangerSchemaViewData)
        }

    }

    override fun parseSchema() {
        val title = smartSettingCreatorView.getSmartSettingTitle()

        if(isValidInputs()) {
            smartSettingCreator.parseSmartSettingSchemaAndCreate(smartSettingShemaViewData.id, object : SmartSettingCreatorCallback {

                override fun getContextListenerCriteriaData(
                    contextListenerTypes: List<Pair<ContextListenerSchemaDBModel, String?>>,
                    criteriaDataCallback: (Map<ContextListenerSchemaDBModel, Any>) -> Unit
                ) {
                    val contextListenerInputMap = mutableMapOf<ContextListenerSchemaDBModel, Any>()
                    for(contextListenerPair in contextListenerTypes) {
                        val smartSettingInputView = contextListenerInputViews[ContextListenerSchemaViewData(contextListenerPair.first.type, contextListenerPair.first.input)]
                        smartSettingInputView?.let {
                            contextListenerInputMap[contextListenerPair.first] = it.getInput()
                        }
                    }

                    criteriaDataCallback(contextListenerInputMap)
                }

                override fun getSettingChangerActionData(
                    settingChangerTypes: List<Pair<SettingChangerSchemaDBModel, String?>>,
                    actionDataCallback: (Map<SettingChangerSchemaDBModel, Any>) -> Unit
                ) {
                    val settingChangerInputMap = mutableMapOf<SettingChangerSchemaDBModel, Any>()
                    for(settingChangerPair in settingChangerTypes) {
                        val smartSettingInputView = settingChangerInputViews[SettingChangerSchemaViewData(settingChangerPair.first.type, settingChangerPair.first.input)]
                        smartSettingInputView?.let {
                            settingChangerInputMap[settingChangerPair.first] = it.getInput()
                        }
                    }

                    actionDataCallback(settingChangerInputMap)
                }

                override fun getName(nameCallback: (String?) -> Unit) {
                    nameCallback(title)
                }

                override fun onSmartSettingsCreated(smartSetting: SmartSetting) {
                    smartSettingCreatorView.showSuccessAndClose()
                }

                override fun failure() {
                    smartSettingCreatorView.showErrorAndClose()
                }
            })
        }
    }

    private fun isValidInputs(): Boolean {
        for(settingChangerInputView in settingChangerInputViews) {
            if(!settingChangerInputView.value.validate()) {
                return false
            }
        }

        for(contextListenerInputView in contextListenerInputViews) {
            if(!contextListenerInputView.value.validate()) {
                return false
            }
        }

        return true
    }
}