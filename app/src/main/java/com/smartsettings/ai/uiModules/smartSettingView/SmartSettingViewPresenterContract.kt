package com.smartsettings.ai.uiModules.smartSettingView

import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

interface SmartSettingView {

    fun showSmartSettings(smartSettingViewDataList: List<SmartSettingViewData>)

    fun showEmptyView()
}

interface SmartSettingViewPresenter {

    fun setHomeView(smartSettingViewReference: WeakReference<SmartSettingView>)

    fun getSmartSettings(lifecycleOwner: LifecycleOwner)

    fun updateSmartSetting(smartSettingViewData: SmartSettingViewData)

    fun deleteSmartSetting(smartSettingViewData: SmartSettingViewData)
}