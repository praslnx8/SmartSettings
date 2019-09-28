package com.smartsettings.ai.models.contextListeners

abstract class ContextListener<out T> {

    abstract fun startListeningToContextChanges(contextChangeCallback: (T) -> Unit)

    abstract fun stopListeningToContextChanges()

    abstract fun askListeningPermissionIfAny(permissionGrantCallback: (Boolean) -> Unit)
}