package com.smartsettings.ai.uiModules.smartSettingProvider

import android.content.Context
import android.view.View
import com.smartsettings.ai.core.smartSettings.SmartSetting

interface SmartSettingProvider {

    fun getView(context: Context, getSmartSetting: (SmartSetting<out Any>) -> Unit): View
}