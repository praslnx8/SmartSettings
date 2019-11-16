package com.smartsettings.ai.uiModules.smartSettingCreatorView

import core.ContextListenerType
import core.SettingChangerType
import java.lang.ref.WeakReference

interface SmartSettingCreatorView {

    fun showSmartSettingSchemas(smartSettingSchemas: List<SmartSettingSchemaViewData>)

    fun showLoading()

    fun showUnableToFetchInfo()

    fun askName(nameCallback: (String?) -> Unit)

    fun askCriteriaData(contextListenerType: Pair<ContextListenerType,String?>, criteriaDataCallback: (Any) -> Unit)

    fun askActionData(settingChangerType: Pair<SettingChangerType,String?>, actionDataCallback: (Any) -> Unit)

    fun close()
}

interface SmartSettingCreatorPresenter {

    fun setView(smartSettingCreatorViewReference: WeakReference<SmartSettingCreatorView>)

    fun getSmartSettingSchemas()

    fun parseSmartSettingSchema(smartSettingSchemaViewData: SmartSettingSchemaViewData)
}