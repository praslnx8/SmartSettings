package com.smartsettings.ai.uiModules.smartSettingCreatorView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.smartsettings.ai.R
import com.smartsettings.ai.data.actionData.VolumeActionData
import com.smartsettings.ai.data.criteriaData.LocationData
import com.smartsettings.ai.ext.inTransaction
import com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView.LocationInputFragment
import com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView.PhoneVolumeInputFragment
import com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView.SmartSettingInputView
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.ContextListenerSchemaViewData
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.SettingChangerSchemaViewData
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.SmartSettingSchemaViewData
import core.ContextListenerType
import core.SettingChangerType
import kotlinx.android.synthetic.main.activity_smart_setting_creator.*


class SmartSettingCreatorActivity : AppCompatActivity(), SmartSettingCreatorView {

    private var smartSettingSchemaViewData: SmartSettingSchemaViewData? = null

    companion object {

        const val SCHEMA_DATA_STR = "schema_data"

        fun open(activity : Activity, smartSettingSchemaViewData: SmartSettingSchemaViewData, reqCode : Int) {
            val intent = Intent(activity, SmartSettingCreatorActivity::class.java)
            intent.putExtra(SCHEMA_DATA_STR, Gson().toJson(smartSettingSchemaViewData))
            activity.startActivityForResult(intent, reqCode)
        }
    }

    private val smartSettingCreatorPresenter: SmartSettingCreatorViewModel by lazy {
        ViewModelProviders.of(this).get(SmartSettingCreatorViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_setting_creator)
        inputFragmentsLayout.removeAllViews()

        smartSettingCreatorPresenter.setView(this)

        val schemaDataStr = intent.getStringExtra(SCHEMA_DATA_STR)
        smartSettingSchemaViewData = Gson().fromJson(schemaDataStr, SmartSettingSchemaViewData::class.java)

        activateButton.setOnClickListener {
            smartSettingCreatorPresenter.parseSchema()
        }

        smartSettingSchemaViewData?.let {
            smartSettingCreatorPresenter.setSchema(it)
        }

        titleText.setOnClickListener {
            titleEditText.visibility = View.VISIBLE
            titleText.visibility = View.GONE
        }
    }

    override fun addInputView(contextListenerSchemaViewData: ContextListenerSchemaViewData): SmartSettingInputView<out Any> {

        inputFragmentsLayout.addView(LayoutInflater.from(this).inflate(R.layout.view_divider, null))

        return if (contextListenerSchemaViewData.type == ContextListenerType.LOCATION_LISTENER) {
            val inputData = if (contextListenerSchemaViewData.input != null) {
                Gson().fromJson(contextListenerSchemaViewData.input, LocationData::class.java)
            } else {
                null
            }
            val locationInputFragment = LocationInputFragment(inputData, contextListenerSchemaViewData.description)
            supportFragmentManager.inTransaction {
                add(
                    inputFragmentsLayout.id,
                    locationInputFragment,
                    contextListenerSchemaViewData.type.name
                )
            }
            locationInputFragment
        } else {
            throw IllegalArgumentException("Unsupported view")
        }
    }

    override fun addInputView(settingChangerSchemaViewData: SettingChangerSchemaViewData): SmartSettingInputView<out Any> {
        return if (settingChangerSchemaViewData.type == SettingChangerType.VOLUME_CHANGER) {
            val inputData = if (settingChangerSchemaViewData.input != null) {
                Gson().fromJson(settingChangerSchemaViewData.input, VolumeActionData::class.java)
            } else {
                null
            }
            val phoneVolumeInputFragment = PhoneVolumeInputFragment(inputData, settingChangerSchemaViewData.description)
            supportFragmentManager.inTransaction {
                add(
                    inputFragmentsLayout.id,
                    phoneVolumeInputFragment,
                    settingChangerSchemaViewData.type.name
                )
            }
            phoneVolumeInputFragment
        } else {
            throw IllegalArgumentException("Unsupported view")
        }
    }

    override fun getSmartSettingTitle(): String {
        return titleEditText.text.toString()
    }

    override fun setSmartSettingData(title: String, description: String?) {
        titleText.text = title
        titleEditText.setText(title)
        descText.text = description?:""
    }

    override fun showErrorAndClose() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun showSuccessAndClose() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}