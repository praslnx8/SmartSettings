package com.smartsettings.ai.uiModules.home

import androidx.lifecycle.LifecycleOwner
import com.smartsettings.ai.core.smartSettings.SmartSetting

interface HomeView {

    fun showSmartSettings(smartSettings: List<SmartSetting<out Any>>)
}

interface HomePresenter {

    fun setHomeView(homeView: HomeView)

    fun getSmartSettings(lifecycleOwner: LifecycleOwner)

    fun smartSettingChangedFromUser(smartSetting: SmartSetting<out Any>)
}