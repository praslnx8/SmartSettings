package com.smartsettings.ai.uiModules.smartSettingCreator

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.core.settingChangers.VolumeSettingChanger
import com.smartsettings.ai.core.smartSettings.LocationBasedSmartSetting
import com.smartsettings.ai.core.smartSettings.SmartSettingType
import com.smartsettings.ai.core.smartSettings.WifiBasedSmartSetting
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.utils.InputDialogUtils

class SmartSettingCreatorActivity : AppCompatActivity(), SmartSettingCreatorView {

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, SmartSettingCreatorActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var smartSettingCreatorPresenter: SmartSettingCreatorPresenter? = null

    private var otherDialog: Dialog? = null

    override fun showChooseSmartSettingMenu() {
        smartSettingsMenuDialog.show()
    }

    override fun showInputUIForSmartSettingType(smartSettingType: SmartSettingType) {
        if (smartSettingType == SmartSettingType.LOCATION_BASED_SETTING) {
            otherDialog = InputDialogUtils.ask(
                this,
                "Enter Details",
                "OK",
                arrayOf("Name", "Latitude", "Longitide", "Radius")
            ) { isPositve, valueArray ->

                if (isPositve) {
                    try {
                        val name = valueArray[0]
                        val lat = valueArray[1].toDouble()
                        val lon = valueArray[2].toDouble()
                        val radius = valueArray[3].toInt()

                        val locationBasedSmartSetting = LocationBasedSmartSetting(name, LocationData(lat, lon, radius))
                        smartSettingCreatorPresenter?.onSmartSettingCreated(locationBasedSmartSetting)
                        true
                    } catch (e: NumberFormatException) {
                        false
                    }
                } else {
                    true
                }
            }

            otherDialog?.show()
        } else if (smartSettingType == SmartSettingType.WIFI_BASED_SETTING) {
            otherDialog = InputDialogUtils.ask(
                this,
                "Enter Details",
                "OK",
                arrayOf("Name", "SSID")
            ) { isPositve, valueArray ->

                if (isPositve) {
                    try {
                        val name = valueArray[0]
                        val ssid = valueArray[1]

                        val wifiBasedSmartSetting = WifiBasedSmartSetting(name, ssid)
                        smartSettingCreatorPresenter?.onSmartSettingCreated(wifiBasedSmartSetting)
                        true
                    } catch (e: NumberFormatException) {
                        false
                    }
                } else {
                    true
                }
            }

            otherDialog?.show()
        }
    }

    override fun showChooseSettingChangerMenu() {
        settingChangersMenu.show()
    }

    override fun showSettingChangerInputUI(settingChangerType: SettingChangerType) {
        if (settingChangerType == SettingChangerType.VOLUME_CHANGER) {
            otherDialog =
                InputDialogUtils.ask(this, "Enter Details", "OK", arrayOf("Volume to set")) { isPositve, valueArray ->

                    if (isPositve) {
                        try {
                            val volumeToSet = valueArray[0].toInt()

                            val volumeSettingChanger = VolumeSettingChanger(VolumeActionData(volumeToSet))

                            smartSettingCreatorPresenter?.onSettingChangersCreated(setOf(volumeSettingChanger))
                            true
                        } catch (e: NumberFormatException) {
                            false
                        }
                    } else {
                        true
                    }
                }

            otherDialog?.show()
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

    private val settingChangersMenu by lazy {
        AlertDialog.Builder(this)
            .setItems(arrayOf("CHANGE VOLUME")) { p0, p1 ->
                if (p1 == 0) {
                    smartSettingCreatorPresenter?.onSettingChangerSelected(SettingChangerType.VOLUME_CHANGER)
                }

                p0.dismiss()
            }
            .create()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (smartSettingsMenuDialog.isShowing) {
            smartSettingsMenuDialog.dismiss()
        }
        if (settingChangersMenu.isShowing) {
            settingChangersMenu.dismiss()
        }
        otherDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }
}