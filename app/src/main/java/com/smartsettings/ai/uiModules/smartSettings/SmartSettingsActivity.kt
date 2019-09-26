package com.smartsettings.ai.uiModules.smartSettings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.smartsettings.ai.R
import com.smartsettings.ai.models.smartSettingProvider.LocationSmartSettingProvider
import kotlinx.android.synthetic.main.activity_add_settings.*

class SmartSettingsActivity : AppCompatActivity() {

    companion object {
        fun open(act: Activity, reqCode: Int) {
            val intent = Intent(act, SmartSettingsActivity::class.java)
            act.startActivityForResult(intent, reqCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_settings)

        val viewModel = ViewModelProviders.of(this).get(SmartSettingsViewModel::class.java)

        container.addView(LocationSmartSettingProvider().getView(this) {
            viewModel.addSmartSetting(it)
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        })
    }
}