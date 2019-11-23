package com.smartsettings.ai.core.contextListeners

import com.smartsettings.ai.core.serializables.SerializableData
import org.junit.Before
import org.junit.Test

class ContextListenerTest {


    @Before
    fun setup() {

    }

    @Test
    fun check_start_listen_to_context_change() {

        var isStartListenCalled = false

        val contextListener = object : ContextListener<Any>(SerializableData("")) {

            override fun startListeningToContextChanges() {
                isStartListenCalled = true
            }

            override fun stopListeningToContextChanges() {
            }

            override fun isCriteriaMatches(criteriaData: Any): Boolean {
                return true
            }

            override fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit) {
            }

            override fun isListeningPermissionGranted(): Boolean {
                return true
            }
        }

        contextListener.startListeningToContextChanges({},{})

        assert(isStartListenCalled)
    }

    @Test
    fun check_on_context_change_with_no_criteira_match_calls_only_context_change_callback() {

        var onContextChangeCalled = false
        var onCriteriaChangeCallbackCalled = false

        val contextListener = object : ContextListener<Any>(SerializableData("")) {

            override fun startListeningToContextChanges() {
                onContextChange()
            }

            override fun stopListeningToContextChanges() {
            }

            override fun isCriteriaMatches(criteriaData: Any): Boolean {
                return false
            }

            override fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit) {
            }

            override fun isListeningPermissionGranted(): Boolean {
                return true
            }
        }

        contextListener.startListeningToContextChanges({
            onCriteriaChangeCallbackCalled = true
        },{
            onContextChangeCalled = true
        })

        assert(onContextChangeCalled)
        assert(!onCriteriaChangeCallbackCalled)
    }

    @Test
    fun check_on_context_change_calls_and_criteria_match_calls_criteria_match_callback() {

        var onContextChangeCalled = false
        var onCriteriaChangeCallbackCalled = false

        val contextListener = object : ContextListener<Any>(SerializableData("")) {

            override fun startListeningToContextChanges() {
                onContextChange()
            }

            override fun stopListeningToContextChanges() {
            }

            override fun isCriteriaMatches(criteriaData: Any): Boolean {
                return true
            }

            override fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit) {
            }

            override fun isListeningPermissionGranted(): Boolean {
                return true
            }
        }

        contextListener.startListeningToContextChanges({
            onCriteriaChangeCallbackCalled = true
        },{
            onContextChangeCalled = true
        })

        assert(onContextChangeCalled)
        assert(onCriteriaChangeCallbackCalled)
    }

    @Test
    fun check_ask_listening_permission_calls_permission_granted_callback_on_permission_granted() {

        var askListeningPermissionGranted = false

        val contextListener = object : ContextListener<Any>(SerializableData("")) {

            override fun startListeningToContextChanges() {
                onContextChange()
            }

            override fun stopListeningToContextChanges() {
            }

            override fun isCriteriaMatches(criteriaData: Any): Boolean {
                return true
            }

            override fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit) {

            }

            override fun isListeningPermissionGranted(): Boolean {
                return true
            }
        }

        contextListener.askListeningPermissionIfAny {
            askListeningPermissionGranted = it
        }

        assert(askListeningPermissionGranted)
    }

    @Test
    fun check_ask_listening_permission_if_any_calls_ask_listening_permission_and_gives_false_if_denied() {

        var askListeningPermissionGranted:Boolean? = null
        var askListeningPermissionCalled = false

        val contextListener = object : ContextListener<Any>(SerializableData("")) {

            override fun startListeningToContextChanges() {
                onContextChange()
            }

            override fun stopListeningToContextChanges() {
            }

            override fun isCriteriaMatches(criteriaData: Any): Boolean {
                return true
            }

            override fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit) {
                askListeningPermissionCalled = true
                permissionGrantCallback(false)
            }

            override fun isListeningPermissionGranted(): Boolean {
                return false
            }
        }

        contextListener.askListeningPermissionIfAny {
            askListeningPermissionGranted = it
        }

        assert(askListeningPermissionCalled)
        assert(askListeningPermissionGranted == false)
    }
}