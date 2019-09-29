package com.smartsettings.permissionhelper

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object PermissionManager {

    val permissionLiveData: MutableLiveData<PermissionResult> = MutableLiveData<PermissionResult>()

    private val permissionResultCallbacks = HashMap<Int, ((PermissionResult) -> Unit)?>()

    private val observer = Observer<PermissionResult> {
        permissionResultCallbacks[it.reqCode]?.invoke(it)
        permissionResultCallbacks.remove(it.reqCode)
    }

    init {
        permissionLiveData.observeForever(observer)
    }

    fun requestPermission(
        context: Context,
        reqCode: Int,
        permissions: Array<String>,
        permissionResultCallback: ((PermissionResult) -> Unit)
    ) {
        permissionResultCallbacks[reqCode] = permissionResultCallback
        PermissionActivity.open(context, reqCode, permissions)
    }
}