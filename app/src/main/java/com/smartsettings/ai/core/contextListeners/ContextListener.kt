package com.smartsettings.ai.core.contextListeners

interface ContextListener {

    fun startListeningToContextChanges(contextChangeCallback: () -> Unit)

    fun stopListeningToContextChanges()

    fun getContextData(): Any?

    fun askListeningPermissionIfAny(permissionGrantCallback: (Boolean) -> Unit)
}