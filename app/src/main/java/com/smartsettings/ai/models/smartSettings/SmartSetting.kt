package com.smartsettings.ai.models.smartSettings

import android.content.Context
import android.view.View
import com.smartsettings.ai.models.changedData.ChangedData

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

    fun onChange(changedData: ChangedData) {
        if (criteriaMatching(changedData)) {
            applyChanges()
        }
    }

    protected abstract fun applyChanges()

    protected abstract fun askPermissions()

    protected abstract fun listenForChanges()

    protected abstract fun stopListeningChanges()

    protected abstract fun criteriaMatching(changedData: ChangedData): Boolean

    abstract fun getView(context: Context): View
}