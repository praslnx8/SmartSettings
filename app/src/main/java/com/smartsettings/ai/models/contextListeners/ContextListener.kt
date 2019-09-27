package com.smartsettings.ai.models.contextListeners

import com.smartsettings.ai.models.contextData.ContextData

abstract class ContextListener<out T> {

    abstract fun startListeningToContextChanges(contextChangeCallback: (ContextData<T>) -> Unit)

    abstract fun stopListeningToContextChanges()

    abstract fun askListeningPermissionIfAny(permissionGrantCallback: (Boolean) -> Unit)
}