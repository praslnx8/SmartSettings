package com.smartsettings.ai.core.smartSettings

import androidx.annotation.StringDef
import com.smartsettings.ai.core.contextListeners.ContextListener
import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.di.DependencyProvider
import com.smartsettings.ai.utils.AndroidNotificationUtil

class SmartSetting(
    var id: Long?,
    val name: String,
    val contextListeners: Set<ContextListener<out Any>>,
    val settingChangers: Set<SettingChanger<out Any>>,
    @ConjunctionLogic val conjunctionLogic: String = AND
) {

    companion object {

        @StringDef(AND, OR)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ConjunctionLogic

        const val AND = "AND"
        const val OR = "OR"
    }

    private var isRunning = false

    private var isEnabled = false

    private var changesAppliedTime = 0L

    var isShowNotificationOnTrigger = false

    private var settingChangesCallback: ((SmartSetting) -> Unit)? = null

    fun isRunning(): Boolean {
        return isRunning
    }

    fun setEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
        reset()
    }

    private fun reset() {
        changesAppliedTime = 0L
    }

    fun isEnabled(): Boolean {
        return isEnabled
    }

    private fun isChangesApplied(): Boolean {
        return changesAppliedTime > 0L
    }

    fun getChangesAppliedTime(): Long {
        return changesAppliedTime
    }

    fun setChangesCallback(settingChangesCallback: ((SmartSetting) -> Unit)) {
        this.settingChangesCallback = settingChangesCallback
    }

    fun isListeningPermissionGranted(): Boolean =
        contextListeners.any { it.isListeningPermissionGranted() }

    fun start() {
        if (isEnabled && !isRunning) {
            askPermissions { isPermissionGranted ->
                if (isPermissionGranted) {
                    isRunning = true
                    contextListeners.forEach {
                        it.startListeningToContextChanges({
                            onContextChangeOfCriteria()
                        }, {
                            onContextChange()
                        })
                    }
                    settingChangesCallback?.invoke(this)
                } else {
                    isEnabled = false
                    settingChangesCallback?.invoke(this)
                }
            }
        }
    }

    private fun onContextChange() {
        if (!isCriteriaMatches()) {
            changesAppliedTime = 0L
        }
    }

    private fun onContextChangeOfCriteria() {

        if (!isChangesApplied()) {
            if (isCriteriaMatches()) {
                if (isShowNotificationOnTrigger) {
                    showNotification()
                }
                settingChangers.forEach {
                    it.applyChanges()
                }
                changesAppliedTime = System.currentTimeMillis()

                settingChangesCallback?.invoke(this)
            }
        }
    }

    private fun showNotification() {
        val notificationTitle = "Setting applied"
        val notificationText = name
        val channelId = "setting_trigger_notification"

        AndroidNotificationUtil.showNotification(
            DependencyProvider.getContext,
            notificationTitle,
            notificationText,
            channelId
        )
    }

    private fun isCriteriaMatches(): Boolean {
        var isCriteriaMatches = conjunctionLogic != OR

        for (contextListener in contextListeners) {
            if (conjunctionLogic == OR && contextListener.isCriteriaMatches()) {
                isCriteriaMatches = true
                break
            } else if (conjunctionLogic == AND && !contextListener.isCriteriaMatches()) {
                isCriteriaMatches = false
                break
            }
        }

        return isCriteriaMatches
    }

    private fun askPermissions(permissionCallback: (Boolean) -> Unit) {

        askContextListenerPermission(contextListeners.iterator()) { isListeningPermissionGranted ->
            if (isListeningPermissionGranted) {
                askSettingChangePermission(settingChangers.iterator()) { isSettingChangePermissionGranted ->
                    permissionCallback(isSettingChangePermissionGranted)
                }
            } else {
                permissionCallback(false)
            }
        }
    }

    private fun askSettingChangePermission(
        iterator: Iterator<SettingChanger<out Any>>,
        callBack: (Boolean) -> Unit
    ) {
        if (iterator.hasNext()) {
            iterator.next().askSettingChangePermissionIfAny {
                if (it) {
                    askSettingChangePermission(iterator, callBack)
                } else {
                    callBack(false)
                }
            }
        } else {
            callBack(true)
        }
    }

    private fun askContextListenerPermission(
        iterator: Iterator<ContextListener<out Any>>,
        callBack: (Boolean) -> Unit
    ) {
        if (iterator.hasNext()) {
            iterator.next().askListeningPermissionIfAny {
                if (it) {
                    askContextListenerPermission(iterator, callBack)
                } else {
                    callBack(false)
                }
            }
        } else {
            callBack(true)
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            contextListeners.forEach {
                it.stopListeningToContextChanges()
            }
            settingChangesCallback?.invoke(this)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is SmartSetting) {
            return id == other.id
        }

        return false
    }

    override fun hashCode(): Int {
        return (id ?: 0).hashCode()
    }
}