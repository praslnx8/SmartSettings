package com.smartsettings.ai.core.contextListeners

import com.smartsettings.ai.core.serializables.SerializableData

abstract class ContextListener<T>(val serializableCriteriaData: SerializableData<out T>) {

    private lateinit var criteriaMatchedCallback: () -> Unit

    private var isCriteriaMatches = false

    fun startListeningToContextChanges(criteriaMatchedCallback: () -> Unit) {
        this.criteriaMatchedCallback = criteriaMatchedCallback
        startListeningToContextChanges()
    }

    protected abstract fun startListeningToContextChanges()

    abstract fun stopListeningToContextChanges()

    /**
     * Should be called when there is context change happens from the listener.
     * This method should be called by the child classes.
     */
    protected fun onContextChange() {
        if (isCriteriaMatches(serializableCriteriaData.data)) {
            isCriteriaMatches = true
            criteriaMatchedCallback()
        } else {
            isCriteriaMatches = false
        }
    }

    /**
     * Check the criteriaData against ContextData that is fetched from the ContextListener
     */
    protected abstract fun isCriteriaMatches(criteriaData: T): Boolean

    protected abstract fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit)

    abstract fun isListeningPermissionGranted(): Boolean

    fun askListeningPermissionIfAny(permissionGrantCallback: (Boolean) -> Unit) {
        if (isListeningPermissionGranted()) {
            permissionGrantCallback(true)
        } else {
            askListeningPermission(permissionGrantCallback)
        }
    }

    fun isCriteriaMatches(): Boolean {
        return isCriteriaMatches
    }
}