package com.smartsettings.ai.uiModules.smartSettingCreatorView

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.smartsettings.ai.R
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.ext.inTransaction
import com.smartsettings.ai.uiModules.smartSettingCreatorView.criteriaDataUI.LocationCriteriaDataActivity
import com.smartsettings.ai.utils.InputDialogUtils
import core.ContextListenerType
import core.SettingChangerType
import kotlinx.android.synthetic.main.activity_smart_setting_creator.*
import java.lang.ref.WeakReference

class SmartSettingCreatorActivity : AppCompatActivity(), SmartSettingCreatorView {

    private val reqForLocData = 1

    private var criteriaDataCallback: ((Any) -> Unit)? = null
    private var actionDataCallback: ((Any) -> Unit)? = null

    private lateinit var smartSettingCreatorListFragment: SmartSettingCreatorListFragment

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, SmartSettingCreatorActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val smartSettingCreatorPresenter: SmartSettingCreatorPresenter by lazy {
        ViewModelProviders.of(this).get(SmartSettingCreatorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_setting_creator)
        smartSettingCreatorListFragment = SmartSettingCreatorListFragment {
            smartSettingCreatorPresenter.setView(WeakReference(this))
            smartSettingCreatorPresenter.getSmartSettingSchemas()
        }
        supportFragmentManager.inTransaction {
            add(container.id, smartSettingCreatorListFragment)
        }
    }

    override fun showSmartSettingSchemas(smartSettingSchemas: List<SmartSettingSchemaViewData>) {
        smartSettingCreatorListFragment.showSmartSettingSchemas(smartSettingSchemas) { item, isActivate ->
            if (isActivate) {
                smartSettingCreatorPresenter.parseSmartSettingSchema(item)
            } else {
                supportFragmentManager.inTransaction {
                    add(container.id, SmartSettingSchemaDetailFragment(item) {
                        smartSettingCreatorPresenter.parseSmartSettingSchema(item)
                    })
                    addToBackStack("detail")
                }
            }
        }
    }

    override fun showLoading() {
        smartSettingCreatorListFragment.showLoading()
    }

    override fun showUnableToFetchInfo() {
        smartSettingCreatorListFragment.showUnableToFetchInfo()
    }

    override fun askName(nameCallback: (String?) -> Unit) {
        InputDialogUtils.ask(this, "Enter Name", "OK", arrayOf("Name")) { isPositive, values ->
            if (isPositive) {
                val name = values[0]
                nameCallback(name)
            }
            true
        }.show()
    }

    override fun askCriteriaData(
        contextListenerType: Pair<ContextListenerType, String?>,
        criteriaDataCallback: (Any) -> Unit
    ) {
        this.criteriaDataCallback = criteriaDataCallback
        if (contextListenerType.first == ContextListenerType.LOCATION_LISTENER) {
            if (contextListenerType.second != null) {
                val locationData =
                    Gson().fromJson(contextListenerType.second, LocationData::class.java)
                criteriaDataCallback(locationData)
            } else {
                LocationCriteriaDataActivity.open(this, reqForLocData)
            }
        }
    }

    override fun askActionData(
        settingChangerType: Pair<SettingChangerType, String?>,
        actionDataCallback: (Any) -> Unit
    ) {
        this.actionDataCallback = actionDataCallback
        if (settingChangerType.first == SettingChangerType.VOLUME_CHANGER) {
            if(settingChangerType.second != null) {
                val volumeActionData = Gson().fromJson(settingChangerType.second, VolumeActionData::class.java)
                actionDataCallback(volumeActionData)
            } else {
                InputDialogUtils.askWithMessage(
                    this, "Enter volume to be set",
                    "OK", arrayOf("Enter phone volume")
                ) { isPositive, values ->
                    if (isPositive) {
                        val volume = values[0].toInt()
                        actionDataCallback(VolumeActionData(volumeToBeSet = volume))
                    }
                    true
                }.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == reqForLocData) {
                val lat = data?.getDoubleExtra(LocationCriteriaDataActivity.LAT_STR, 0.0) ?: 0.0
                val lon = data?.getDoubleExtra(LocationCriteriaDataActivity.LON_STR, 0.0) ?: 0.0
                val radius = data?.getIntExtra(LocationCriteriaDataActivity.RADIUS_STR, 0) ?: 0

                criteriaDataCallback?.invoke(LocationData(lat, lon, radius))
            }
        }
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.popBackStackImmediate()) {
            super.onBackPressed()
        }
    }

    override fun close() {
        finish()
    }
}