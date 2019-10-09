package com.smartsettings.ai.core.smartSettings

import androidx.annotation.StringDef
import com.smartsettings.ai.core.contextListeners.ContextListener
import com.smartsettings.ai.core.serializables.SerializableData
import com.smartsettings.ai.core.settingChangers.SettingChanger

abstract class SmartSetting<CRITERIA>(
    val name: String,
    val criteriaData: SerializableData<CRITERIA>,
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

    private var isChangesApplied = false

    private var settingChangesCallback: ((SmartSetting<CRITERIA>) -> Unit)? = null

    private val settingChangers = HashSet<SettingChanger<Any>>()

    private val contextListeners by lazy {
        createContextListeners()
    }

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

    fun setChangesCallback(settingChangesCallback: ((SmartSetting<CRITERIA>) -> Unit)) {
        this.settingChangesCallback = settingChangesCallback
    }

    fun start() {
        if (isEnabled && !isRunning) {
            askPermissions { isPermissionGranted ->
                if (isPermissionGranted) {
                    isRunning = true
                    contextListeners.forEach {
                        it.startListeningToContextChanges {
                            onContextChange()
                        }
                    }
                } else {
                    isEnabled = false
                    settingChangesCallback?.invoke(this)
                }
            }
        }
    }

    private fun onContextChange() {

        val isNewChangesApplied = if (isCriteriaMatches()) {
            settingChangers.forEach {
                it.applyChanges()
            }
            true
        } else {
            false
        }

        if (isChangesApplied != isNewChangesApplied) {
            isChangesApplied = isNewChangesApplied
            settingChangesCallback?.invoke(this)
        }
    }

    private fun isCriteriaMatches(): Boolean {
        var isCriteriaMatches = conjunctionLogic != OR

        for (contextListener in contextListeners) {
            if (conjunctionLogic == OR && criteriaMatchingForContextDataFromListener(
                    criteriaData.data,
                    contextListener
                )
            ) {
                isCriteriaMatches = true
                break
            } else if (conjunctionLogic == AND && !criteriaMatchingForContextDataFromListener(
                    criteriaData.data,
                    contextListener
                )
            ) {
                isCriteriaMatches = false
                break
            }
        }

        return isCriteriaMatches
    }

    private fun askPermissions(permissionCallback: (Boolean) -> Unit) {

        askContextListenerPermission(true, contextListeners.iterator()) { isListeningPermissionGranted ->
            if (isListeningPermissionGranted) {
                askSettingChangePermission(true, settingChangers.iterator()) { isSettingChangePermissionGranted ->
                    permissionCallback(isSettingChangePermissionGranted)
                }
            } else {
                permissionCallback(false)
            }
        }
    }

    private fun askSettingChangePermission(
        previousVal: Boolean,
        iterator: Iterator<SettingChanger<Any>>,
        callBack: (Boolean) -> Unit
    ) {
        if (iterator.hasNext()) {
            iterator.next().askSettingChangePermissionIfAny {
                if (it) {
                    askSettingChangePermission(true, iterator, callBack)
                } else {
                    callBack(false)
                }
            }
        } else {
            callBack(previousVal)
        }
    }

    private fun askContextListenerPermission(
        previousVal: Boolean,
        iterator: Iterator<ContextListener>,
        callBack: (Boolean) -> Unit
    ) {
        if (iterator.hasNext()) {
            iterator.next().askListeningPermissionIfAny {
                if (it) {
                    askContextListenerPermission(true, iterator, callBack)
                } else {
                    callBack(false)
                }
            }
        } else {
            callBack(previousVal)
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

    fun addSettingChangers(settingChangers: Set<SettingChanger<Any>>, isClearPrevious: Boolean = false) {
        if (isClearPrevious) {
            this.settingChangers.clear()
        }
        this.settingChangers.addAll(settingChangers)
    }

    fun getSettingChangers(): Set<SettingChanger<Any>> {
        return settingChangers
    }

    protected abstract fun criteriaMatchingForContextDataFromListener(
        criteria: CRITERIA,
        contextListener: ContextListener
    ): Boolean

    protected abstract fun createContextListeners(): Set<ContextListener>
}