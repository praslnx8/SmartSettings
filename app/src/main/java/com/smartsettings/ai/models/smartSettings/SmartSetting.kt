package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.view.View
import com.smartsettings.ai.models.contextData.ContextData
import com.smartsettings.ai.models.contextListeners.ContextListener

abstract class SmartSetting<out T> {

    private var isRunning = false

    private var isEnabled = false

    fun isRunning(): Boolean {
        return isRunning
    }

    fun setEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled

        if (isEnabled) {
            start()
        } else {
            stop()
        }
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
                        if (criteriaMatching(contextData)) {
                            applyChanges()
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

    protected abstract fun applyChanges()

    protected abstract fun askSettingChangePermissionIfAny(locationGranterCallback: (Boolean) -> Unit)

    protected abstract fun getContextListener(): ContextListener<T>

    protected abstract fun criteriaMatching(contextData: ContextData<T>): Boolean

    abstract fun getView(context: Context): View
}