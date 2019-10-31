package com.smartsettings.ai.uiModules.smartSettingCreator.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smartsettings.ai.R
import com.smartsettings.ai.core.smartSettings.SmartSettingType
import kotlinx.android.synthetic.main.activity_setting_chooser.*

class LocationBasedSmartSettingInputActivity : AppCompatActivity() {

    companion object {

        const val SMART_SETTING_CREATED_STR = "smart_setting_selected"

        fun open(activity: Activity, reqCode: Int) {
            val intent = Intent(activity, LocationBasedSmartSettingInputActivity::class.java)
            activity.startActivityForResult(intent, reqCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_chooser)

        val smartSettingTypes = SmartSettingType.values()

        for (smartSettingType in smartSettingTypes) {
            parentLayout.addView(getItemView(smartSettingType.value, smartSettingType))
        }
    }

    private fun getItemView(name: String, smartSettingType: SmartSettingType): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_choose_smart_setting, parentLayout, false)
        val nameText = view.findViewById<TextView>(R.id.nameText)
        val rootLayout = view.findViewById<ViewGroup>(R.id.rootLayout)

        nameText.text = name
        rootLayout.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(SMART_SETTING_CREATED_STR, smartSettingType.value)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        return view
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}