package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.view.View
import com.smartsettings.ai.models.SettingState
import com.smartsettings.ai.models.changedData.ChangedData

interface SmartSetting {

    fun applyChanges(currentState : SettingState) : SettingState

    fun askPermissions()

    fun listenForChanges()

    fun stopListeningChanges()

    fun isRunning(): Boolean

    fun criteriaMatching(changedData: ChangedData): Boolean

    fun getView(context: Context): View
}