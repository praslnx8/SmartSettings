package com.smartsettings.permissionhelper

import android.content.pm.PackageManager

data class PermissionResult(
    val reqCode: Int,
    val permissions: Array<out String>,
    val grantResults: IntArray
) {
    fun isAllGranted(): Boolean {
        var isGranted = true
        for (i in grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                isGranted = false
            }
        }

        return isGranted
    }
}