package com.smartsettings.ai.uiModules.smartSettingView

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.uiModules.uiModels.SmartSettingViewData
import javax.inject.Inject

class SmartSettingViewModel : ViewModel(), SmartSettingViewPresenter {

    @Inject
    lateinit var smartSettingRepository: SmartSettingRepository

    init {
        SmartApp.appComponent.inject(this)
    }

    private val smartSettingLiveData = SmartProfile.getSmartSettingLiveData()

    private var smartSettingView: SmartSettingView? = null

    private val smartSettingViewDataMap = mutableMapOf<Int, SmartSetting<out Any>>()

    override fun setHomeView(smartSettingView: SmartSettingView) {
        this.smartSettingView = smartSettingView
    }

    override fun getSmartSettings(lifecycleOwner: LifecycleOwner) {
        SmartProfile.load(smartSettingRepository)
        smartSettingLiveData.observe(lifecycleOwner, Observer<Set<SmartSetting<out Any>>> {

            val smartSettingViewDataList = ArrayList<SmartSettingViewData>()
            for ((key, smartSetting) in it.withIndex()) {
                val smartSettingViewData = SmartSettingViewData.getSmartSetting(key, smartSetting)
                smartSettingViewDataMap[key] = smartSetting
                smartSettingViewDataList.add(smartSettingViewData)
            }

            if (smartSettingViewDataList.isNotEmpty()) {
                smartSettingView?.showSmartSettings(smartSettingViewDataList)
            } else {
                smartSettingView?.showEmptyView()
            }
        })
    }

    override fun smartSettingChangedFromUser(smartSettingViewData: SmartSettingViewData) {
        smartSettingViewDataMap[smartSettingViewData.key]?.let {
            it.setEnabled(smartSettingViewData.isEnabled)
            SmartProfile.updateSmartSetting(smartSettingRepository, it)
        }
    }

    override fun deleteSmartSetting(smartSettingViewData: SmartSettingViewData) {
        smartSettingViewDataMap[smartSettingViewData.key]?.let {
            SmartProfile.deleteSmartSetting(smartSettingRepository, it)
        }

    }
}