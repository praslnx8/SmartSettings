package com.smartsettings.ai.core.smartSettings

import android.util.Log
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.contextListeners.ContextListener
import com.smartsettings.ai.core.contextListeners.WifiListener
import com.smartsettings.ai.core.serializables.SerializableData
import javax.inject.Inject

class WifiBasedSmartSetting(name: String, wifiSSID: String) :
    SmartSetting<String>(name, SerializableData(wifiSSID)) {

    @Inject
    lateinit var wifiListener: WifiListener

    override fun createContextListeners(): Set<ContextListener> {
        return setOf(wifiListener)
    }

    init {
        SmartApp.appComponent.inject(this)
    }

    override fun criteriaMatchingForContextDataFromListener(
        criteria: String,
        contextListener: ContextListener
    ): Boolean {

        if (contextListener is WifiListener) {
            val contextData = contextListener.getContextData()
            if (contextData != null && contextData.ssid == criteriaData.data) {
                return true
            }
        }

        Log.d("XDFCE", "criteria failed")

        return false
    }
}