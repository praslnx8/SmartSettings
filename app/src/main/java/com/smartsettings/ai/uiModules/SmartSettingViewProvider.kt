package com.smartsettings.ai.uiModules

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.smartsettings.ai.R
import com.smartsettings.ai.models.smartSettings.LocationBasedVolumeSetting
import com.smartsettings.ai.models.smartSettings.SmartSetting

object SmartSettingViewProvider {

    fun getView(
        context: Context,
        smartSetting: SmartSetting<out Any, out Any, out Any>,
        changesCallback: ((SmartSetting<out Any, out Any, out Any>) -> Unit)
    ): View {

        if (smartSetting is LocationBasedVolumeSetting) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_loc_smart_setting, null)

            val nameText = view.findViewById<TextView>(R.id.nameText)
            val switchView = view.findViewById<Switch>(R.id.switchView)
            val doneView = view.findViewById<ImageView>(R.id.doneView)

            nameText.text = smartSetting.name

            switchView.isChecked = smartSetting.isRunning()

            switchView.setOnCheckedChangeListener { _, isEnable ->
                Log.d("XDFCE", "on check called")
                smartSetting.setEnabled(isEnable)
                changesCallback(smartSetting)
            }

            if (smartSetting.isChangesApplied()) {
                doneView.visibility = View.VISIBLE
            } else {
                doneView.visibility = View.GONE
            }

            return view
        }

        throw IllegalArgumentException("Type is not matching")
    }
}