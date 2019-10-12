package com.smartsettings.ai.core.contextListeners

abstract class ContextListener {

    abstract fun startListeningToContextChanges(contextChangeCallback: () -> Unit)

    abstract fun stopListeningToContextChanges()

    abstract fun getContextData(): Any?

    protected abstract fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit)

    abstract fun isListeningPermissionGranted(): Boolean

    fun askListeningPermissionIfAny(permissionGrantCallback: (Boolean) -> Unit) {
        if (isListeningPermissionGranted()) {
            permissionGrantCallback(true)
        } else {
            askListeningPermission(permissionGrantCallback)
        }
    }
}