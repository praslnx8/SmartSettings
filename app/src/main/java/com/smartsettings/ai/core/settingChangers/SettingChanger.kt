package com.smartsettings.ai.core.settingChangers

import com.smartsettings.ai.core.serializables.SerializableData

abstract class SettingChanger<out T>(val serializableActionData: SerializableData<out T>) {

    val actionData = serializableActionData.data

    abstract fun askSettingChangePermissionIfAny(permissionGrantCallback: (Boolean) -> Unit)

    abstract fun applyChanges()
}