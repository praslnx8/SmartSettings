package com.smartsettings.ai.uiModules.smartSettingView

import androidx.lifecycle.LifecycleOwner
import com.smartsettings.ai.uiModules.uiModels.SmartSettingViewData

interface SmartSettingView {

    fun showSmartSettings(smartSettingViewDataList: List<SmartSettingViewData>)

    fun showEmptyView()
}

interface SmartSettingViewPresenter {

    fun setHomeView(smartSettingView: SmartSettingView)

    fun getSmartSettings(lifecycleOwner: LifecycleOwner)

    fun updateSmartSetting(smartSettingViewData: SmartSettingViewData)

    fun deleteSmartSetting(smartSettingViewData: SmartSettingViewData)
}