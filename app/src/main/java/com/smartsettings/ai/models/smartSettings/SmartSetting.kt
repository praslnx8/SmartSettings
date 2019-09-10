package com.smartsettings.ai.models.smartSettings

import com.smartsettings.ai.models.SettingState
import com.smartsettings.ai.models.changedData.ChangedData

interface SmartSetting {

    fun applyChanges(currentState : SettingState) : SettingState

    fun askPermissions()

    fun criteriaMatching(changedData: ChangedData): Boolean
}