package com.smartsettings.ai.runner

import android.util.Log
import androidx.lifecycle.Observer
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.smartSettings.SmartSetting

object MainService {

    private val observer = Observer<Set<SmartSetting>> { smartSettings ->
        smartSettings?.forEach { smartSetting ->
            Log.d("XDFCE", "service refreshed")
            if (smartSetting.isEnabled()) {
                smartSetting.start()
            } else {
                smartSetting.stop()
            }
        }
    }

    fun onCreate() {
        SmartProfile.getSmartSettingListLiveData().observeForever(observer)
    }


    fun onDestroy() {
        SmartProfile.getSmartSettingListLiveData().removeObserver(observer)
    }
}