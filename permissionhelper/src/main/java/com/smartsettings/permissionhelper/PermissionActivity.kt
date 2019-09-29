package com.smartsettings.permissionhelper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PermissionActivity : AppCompatActivity() {

    companion object {

        const val REQ_CODE_STR = "req_code"
        const val PERMISSIONS_STR = "permissions"

        fun open(context: Context, reqCode: Int, permissions: Array<String>) {
            val intent = Intent(context, PermissionActivity::class.java)

            intent.putExtra(REQ_CODE_STR, reqCode)
            intent.putExtra(PERMISSIONS_STR, permissions)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }

    var reqCode: Int? = null
    var permissions: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reqCode = intent.getIntExtra(REQ_CODE_STR, 0)
        permissions = intent.getStringArrayExtra(PERMISSIONS_STR)

        require(permissions?.isEmpty() != true) { "permissions are empty" }

        val ps = permissions
        val rCode = reqCode

        if (ps != null && rCode != null) {
            requestPermissions(ps, rCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val permissionResult = PermissionResult(requestCode, permissions, grantResults)

        PermissionManager.permissionLiveData.value = permissionResult

        finish()
    }
}