package com.smartsettings.ai.models.smartSettingProvider

import android.content.Context
import android.view.View
import com.smartsettings.ai.models.smartSettings.SmartSetting

interface SmartSettingProvider {

    fun getView(context: Context, getSmartSetting: (SmartSetting<out Any, out Any, out Any>) -> Unit): View
}