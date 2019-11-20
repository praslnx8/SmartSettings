package com.smartsettings.ai.uiModules.smartSettingSchemaChooserView

import java.lang.ref.WeakReference

interface SmartSettingSchemaView {

    fun showSmartSettingSchemas(smartSettingSchemas: List<SmartSettingSchemaViewData>)

    fun showLoading()

    fun showUnableToFetchInfo()

    fun close()
}

interface SmartSettingSchemaPresenter {

    fun setView(smartSettingSchemaViewReference: WeakReference<SmartSettingSchemaView>)

    fun getSmartSettingSchemas()
}