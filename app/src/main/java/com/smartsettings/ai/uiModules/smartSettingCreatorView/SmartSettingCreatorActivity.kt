package com.smartsettings.ai.uiModules.smartSettingCreatorView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.smartsettings.ai.R
import com.smartsettings.ai.core.contextListeners.ContextListenerType
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import kotlinx.android.synthetic.main.activity_smart_setting_creator.*
import java.lang.ref.WeakReference

class SmartSettingCreatorActivity : AppCompatActivity(), SmartSettingCreatorView {

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

    override fun askName(nameCallback: (String) -> Unit) {

    }

    override fun askCriteriaData(contextListenerType: ContextListenerType, criteriaDataCallback: (Any) -> Unit) {

    }

    override fun askActionData(settingChangerType: SettingChangerType, actionDataCallback: (Any) -> Unit) {

    }

    override fun close() {

    }
}