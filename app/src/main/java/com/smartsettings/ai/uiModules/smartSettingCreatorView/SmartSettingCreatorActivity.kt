package com.smartsettings.ai.uiModules.smartSettingCreatorView

import android.os.Bundle
import android.os.PersistableBundle

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.smartsettings.ai.R
import com.smartsettings.ai.uiModules.smartSettingCreatorView.inputView.SmartSettingInputView
import com.smartsettings.ai.uiModules.smartSettingSchemaChooserView.SmartSettingSchemaViewData
import core.ContextListenerType
import core.SettingChangerType
import kotlinx.android.synthetic.main.activity_smart_setting_creator.*


class SmartSettingSchemaCreatorActivity(
    private val smartSettingSchemaViewData: SmartSettingSchemaViewData
) : AppCompatActivity(), SmartSettingCreatorView {

    private val smartSettingCreatorPresenter: SmartSettingCreatorViewModel by lazy {
        ViewModelProviders.of(this).get(SmartSettingCreatorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_smart_setting_creator)

        activateButton.setOnClickListener {
            smartSettingCreatorPresenter.parseSchema()
        }

        smartSettingCreatorPresenter.setSchema(smartSettingSchemaViewData)
    }

    override fun addInputView(contextListenerType: ContextListenerType): SmartSettingInputView<out Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addInputView(settingChangerType: SettingChangerType): SmartSettingInputView<out Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSmartSettingTitle(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSmartSettingData(title: String, description: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}