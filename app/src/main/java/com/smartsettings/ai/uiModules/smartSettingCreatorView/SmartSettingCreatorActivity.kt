package com.smartsettings.ai.uiModules.smartSettingCreatorView

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartsettings.ai.R
import com.smartsettings.ai.core.contextListeners.ContextListenerType
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.uiModules.smartSettingCreatorView.criteriaDataUI.LocationCriteriaDataActivity
import kotlinx.android.synthetic.main.activity_smart_setting_creator.*
import java.lang.ref.WeakReference

class SmartSettingCreatorActivity : AppCompatActivity(), SmartSettingCreatorView {

    private val reqForLocData = 1

    private var criteriaDataCallback: ((Any) -> Unit)? = null
    private var actionDataCallback: ((Any) -> Unit)? = null

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
        smartSettingCreatorPresenter.setView(WeakReference(this))
        smartSettingCreatorPresenter.getSmartSettingSchemas()
    }

    override fun showSmartSettingSchemas(smartSettingSchemas: List<SmartSettingSchemaViewData>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SmartSettingCreatorRecyclerViewAdapter(smartSettingSchemas) {
            smartSettingCreatorPresenter.parseSmartSettingSchema(it)
        }
        recyclerView.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    override fun showLoading() {
        recyclerView.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun showUnableToFetchInfo() {
        recyclerView.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun askName(nameCallback: (String?) -> Unit) {
        nameCallback(null)
    }

    override fun askCriteriaData(contextListenerType: ContextListenerType, criteriaDataCallback: (Any) -> Unit) {
        this.criteriaDataCallback = criteriaDataCallback
        if (contextListenerType == ContextListenerType.LOCATION_LISTENER) {
            LocationCriteriaDataActivity.open(this, reqForLocData)
        } else if (contextListenerType == ContextListenerType.WIFI_LISTENER) {

        }
    }

    override fun askActionData(settingChangerType: SettingChangerType, actionDataCallback: (Any) -> Unit) {
        this.actionDataCallback = actionDataCallback
        if (settingChangerType == SettingChangerType.VOLUME_CHANGER) {

        } else if (settingChangerType == SettingChangerType.VOLUME_MUTE_CHANGER) {
            actionDataCallback("")
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

    override fun close() {
        finish()
    }
}