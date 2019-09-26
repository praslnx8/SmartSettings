package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.view.View
import com.smartsettings.ai.models.changedData.ContextData

abstract class SmartSetting {

    private var isRunning = false

    fun isRunning(): Boolean {
        return isRunning
    }

    fun start() {
        isRunning = true
        listenForChanges()
    }

    fun stop() {
        isRunning = false
        stopListeningChanges()
    }

    fun onChange(contextData: ContextData) {
        if (criteriaMatching(contextData)) {
            applyChanges()
        }
    }

    protected abstract fun applyChanges()

    protected abstract fun askPermissions()

    protected abstract fun listenForChanges()

    protected abstract fun stopListeningChanges()

    protected abstract fun criteriaMatching(contextData: ContextData): Boolean

    abstract fun getView(context: Context): View
}