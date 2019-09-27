package com.smartsettings.ai.models.contextData

interface ContextData<out T> {

    fun getChangedData(): T
}