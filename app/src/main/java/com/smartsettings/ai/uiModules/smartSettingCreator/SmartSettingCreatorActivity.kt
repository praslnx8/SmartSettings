package com.smartsettings.ai.uiModules.smartSettingCreator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.core.smartSettings.SmartSettingType
import com.smartsettings.ai.uiModules.smartSettingCreator.views.SmartSettingChooserActivity

class SmartSettingCreatorActivity : AppCompatActivity(), SmartSettingCreatorView {

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, SmartSettingCreatorActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var smartSettingCreatorPresenter: SmartSettingCreatorPresenter? = null

    private val reqForSmartSettingChooser = 1


    override fun showChooseSmartSettingMenu() {
        SmartSettingChooserActivity.open(this, reqForSmartSettingChooser)
    }

    override fun showInputUIForSmartSettingType(smartSettingType: SmartSettingType) {
        if (smartSettingType == SmartSettingType.LOCATION_BASED_SETTING) {

        } else if (smartSettingType == SmartSettingType.WIFI_BASED_SETTING) {

        }
    }

    override fun showChooseSettingChangerMenu() {

    }

    override fun showSettingChangerInputUI(settingChangerType: SettingChangerType) {
        if (settingChangerType == SettingChangerType.VOLUME_CHANGER) {

        }
    }

    override fun close() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        smartSettingCreatorPresenter = ViewModelProviders.of(this).get(SmartSettingCreatorViewModel::class.java)
        smartSettingCreatorPresenter?.setView(this)
        smartSettingCreatorPresenter?.onViewLoaded()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == reqForSmartSettingChooser) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            } else {
                val smartSettingTypeValStr =
                    data?.getStringExtra(SmartSettingChooserActivity.SMART_SETTING_SELECTED_STR)
                if (smartSettingTypeValStr != null) {
                    val smartSettingType = SmartSettingType.valueOf(smartSettingTypeValStr)
                    smartSettingCreatorPresenter?.onSmartSettingTypeSelected(smartSettingType)
                }
            }
        }
    }
}