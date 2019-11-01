package com.smartsettings.ai.core.settingChangers

import com.smartsettings.ai.core.serializables.SerializableData

abstract class SettingChanger<T>(val serializableActionData: SerializableData<out T>) {

    private var isChangesApplied = false

    abstract fun askSettingChangePermissionIfAny(permissionGrantCallback: (Boolean) -> Unit)

    abstract fun isPermissionGranted(): Boolean

    fun applyChanges() {
        isChangesApplied = isPermissionGranted() && applySettingChanges(serializableActionData.data)
    }

    protected abstract fun applySettingChanges(actionData: T): Boolean

    fun isChangesApplied(): Boolean {
        return isChangesApplied
    }
}