package com.smartsettings.ai.uiModules.smartSettingCreatorView

import com.smartsettings.ai.core.contextListeners.ContextListenerType
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import java.lang.ref.WeakReference

interface SmartSettingCreatorView {

    fun showSmartSettingSchemas(smartSettingSchemas: List<SmartSettingSchemaViewData>)

    fun showLoading()

    fun showUnableToFetchInfo()

    fun askName(nameCallback: (String) -> Unit)

    fun askCriteriaData(contextListenerType: ContextListenerType, criteriaDataCallback: (Any) -> Unit)

    fun askActionData(settingChangerType: SettingChangerType, actionDataCallback: (Any) -> Unit)

    fun close()
}

interface SmartSettingCreatorPresenter {

    fun setView(smartSettingCreatorViewReference: WeakReference<SmartSettingCreatorView>)

    fun getSmartSettingSchemas()

    fun parseSmartSettingSchema(smartSettingSchemaViewData: SmartSettingSchemaViewData)
}