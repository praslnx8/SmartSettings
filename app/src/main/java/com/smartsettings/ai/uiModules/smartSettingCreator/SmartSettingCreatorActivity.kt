package com.smartsettings.ai.uiModules.smartSettingCreator

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class SmartSettingCreatorActivity : AppCompatActivity(), SmartSettingCreatorView {

    private var smartSettingCreatorPresenter: SmartSettingCreatorPresenter? = null

    override fun showChooseSmartSettingMenu() {
        smartSettingsMenuDialog.show()
    }

    override fun showInputUIForSmartSettingType(smartSettingType: SmartSettingType) {

    }

    override fun showChooseSettingChangerMenu() {

    }

    override fun showSettingChangerInputUI(settingChangerType: SettingChangerType) {

    }

    override fun close() {
        finish()
    }

    private val smartSettingsMenuDialog by lazy {
        AlertDialog.Builder(this)
            .setItems(arrayOf("ON LOCATION", "ON WIFI DISCOVERED")) { p0, p1 ->
                if (p1 == 0) {
                    smartSettingCreatorPresenter?.onSmartSettingTypeSelected(SmartSettingType.LOCATION_BASED_SETTING)
                } else if (p1 == 1) {
                    smartSettingCreatorPresenter?.onSmartSettingTypeSelected(SmartSettingType.WIFI_BASED_SETTING)
                }

                p0.dismiss()
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        smartSettingCreatorPresenter = ViewModelProviders.of(this).get(SmartSettingCreatorViewModel::class.java)
        smartSettingCreatorPresenter?.setView(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (smartSettingsMenuDialog.isShowing) {
            smartSettingsMenuDialog.dismiss()
        }
    }
}