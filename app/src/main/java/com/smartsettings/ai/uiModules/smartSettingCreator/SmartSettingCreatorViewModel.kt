package com.smartsettings.ai.uiModules.smartSettingCreator

import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.settingChangers.SettingChanger
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.core.smartSettings.SmartSettingType
import javax.inject.Inject

class SmartSettingCreatorViewModel : ViewModel(), SmartSettingCreatorPresenter {

    private var view: SmartSettingCreatorView? = null

    private var smartSetting: SmartSetting<out Any>? = null

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    init {
        SmartApp.appComponent.inject(this)
    }

    override fun setView(view: SmartSettingCreatorView) {
        this.view = view
    }

    override fun onViewLoaded() {
        view?.showChooseSmartSettingMenu()
    }

    override fun onSmartSettingTypeSelected(smartSettingType: SmartSettingType) {
        view?.showInputUIForSmartSettingType(smartSettingType)
    }

    override fun onSettingChangerSelected(settingChangerType: SettingChangerType) {
        view?.showSettingChangerInputUI(settingChangerType)
    }

    override fun onSmartSettingCreated(smartSetting: SmartSetting<out Any>) {
        this.smartSetting = smartSetting
        view?.showChooseSettingChangerMenu()
    }

    override fun onSettingChangersCreated(settingChangers: Set<SettingChanger<out Any>>) {
        smartSetting?.addSettingChangers(settingChangers)

        smartSetting?.let {
            SmartProfile.addSmartSetting(smartSettingRepository, it)
        }

        view?.close()
    }
}