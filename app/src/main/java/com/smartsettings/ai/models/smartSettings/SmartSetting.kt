package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.view.View
import com.google.gson.Gson
import com.smartsettings.ai.models.contextListeners.ContextListener

abstract class SmartSetting<CONTEXT, CRITERIA, ACTION>(
    private val criteriaData: CRITERIA,
    private val settingData: ACTION
) {

    private var isRunning = false

    private var isEnabled = false

    fun isRunning(): Boolean {
        return isRunning
    }

    fun setEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }

    fun isEnabled(): Boolean {
        return isEnabled
    }

    fun start() {
        if (isEnabled && !isRunning) {
            askPermissions { isPermissionGranted ->
                if (isPermissionGranted) {
                    isRunning = true
                    getContextListener().startListeningToContextChanges { contextData ->
                        if (criteriaMatching(criteriaData, contextData)) {
                            applyChanges(settingData)
                        }
                    }
                }
            }
        }
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
        isRunning = false
        getContextListener().stopListeningToContextChanges()
    }

    fun serializeCriteriaData(): String {
        return Gson().toJson(criteriaData)
    }

    fun serializeActionData(): String {
        return Gson().toJson(settingData)
    }

    protected abstract fun applyChanges(settingData: ACTION)

    protected abstract fun askSettingChangePermissionIfAny(locationGranterCallback: (Boolean) -> Unit)

    protected abstract fun getContextListener(): ContextListener<CONTEXT>

    protected abstract fun criteriaMatching(criteria: CRITERIA, contextData: CONTEXT): Boolean

    abstract fun getView(context: Context): View
}