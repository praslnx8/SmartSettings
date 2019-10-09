package com.smartsettings.ai.uiModules.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.smartSettings.SmartSetting
import javax.inject.Inject

class HomeViewModel : ViewModel(), HomePresenter {

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    init {
        SmartApp.appComponent.inject(this)
    }

    private val smartSettingLiveData = SmartProfile.getSmartSettingLiveData()

    private var homeView: HomeView? = null

    override fun setHomeView(homeView: HomeView) {
        this.homeView = homeView
    }

    override fun getSmartSettings(lifecycleOwner: LifecycleOwner) {
        SmartProfile.load(smartSettingRepository)
        smartSettingLiveData.observe(lifecycleOwner, Observer<Set<SmartSetting<out Any>>> {
            homeView?.showSmartSettings(it.toList())
        })
    }

    override fun smartSettingChangedFromUser(smartSetting: SmartSetting<out Any>) {
        SmartProfile.updateSmartSetting(smartSettingRepository, smartSetting)
    }
}