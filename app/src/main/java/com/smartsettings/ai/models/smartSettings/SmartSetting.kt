package com.smartsettings.ai.models.smartSettings

import com.smartsettings.ai.models.contextListeners.ContextListener
import com.smartsettings.ai.models.serializables.SerializableData

abstract class SmartSetting<CONTEXT, CRITERIA, ACTION>(
    val name: String,
    val criteriaData: SerializableData<CRITERIA>,
    val actionData: SerializableData<ACTION>
) {

    private var isRunning = false

    private var isEnabled = false

    private var isChangesApplied = false

    private var settingChangesCallback: ((SmartSetting<out CONTEXT, out CRITERIA, out ACTION>) -> Unit)? = null

    fun isRunning(): Boolean {
        return isRunning
    }

    fun setEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }

    fun isEnabled(): Boolean {
        return isEnabled
    }

    fun isChangesApplied(): Boolean {
        return isChangesApplied
    }

    fun setChangesCallback(settingChangesCallback: ((SmartSetting<out CONTEXT, out CRITERIA, out ACTION>) -> Unit)) {
        this.settingChangesCallback = settingChangesCallback
    }

    fun start() {
        if (isEnabled && !isRunning) {
            askPermissions { isPermissionGranted ->
                if (isPermissionGranted) {
                    isRunning = true
                    getContextListener().startListeningToContextChanges { contextData ->
                        onContextChange(contextData)
                    }
                } else {
                    isEnabled = false
                    settingChangesCallback?.invoke(this)
                }
            }
        }
    }

    private fun onContextChange(contextData: CONTEXT) {
        isChangesApplied = if (criteriaMatching(criteriaData.data, contextData) && !isChangesApplied) {
            applyChanges(actionData.data)
            true
        } else {
            false
        }
        settingChangesCallback?.invoke(this)
    }

    private fun askPermissions(permissionCallback: (Boolean) -> Unit) {
        getContextListener().askListeningPermissionIfAny { isListenPermissionGranted ->
            if (isListenPermissionGranted) {
                askSettingChangePermissionIfAny { isChangeSettingPermissionGranted ->
                    permissionCallback(isChangeSettingPermissionGranted)
                }
            } else {
                permissionCallback(false)
            }
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            getContextListener().stopListeningToContextChanges()
            settingChangesCallback?.invoke(this)
        }
    }

    protected abstract fun applyChanges(settingData: ACTION)

    protected abstract fun askSettingChangePermissionIfAny(locationGranterCallback: (Boolean) -> Unit)

    protected abstract fun getContextListener(): ContextListener<CONTEXT>

    protected abstract fun criteriaMatching(criteria: CRITERIA, contextData: CONTEXT): Boolean
}