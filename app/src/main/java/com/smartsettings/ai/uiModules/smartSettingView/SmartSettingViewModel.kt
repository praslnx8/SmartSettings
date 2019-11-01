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

    private val smartSettingLiveData = SmartProfile.getSmartSettingLiveData()

    private lateinit var smartSettingView: SmartSettingView

    private val smartSettingViewDataMap = mutableMapOf<Int, SmartSetting>()

    init {
        SmartApp.appComponent.inject(this)
    }

    override fun setHomeView(smartSettingView: SmartSettingView) {
        this.smartSettingView = smartSettingView
    }

    override fun getSmartSettings(lifecycleOwner: LifecycleOwner) {
        SmartProfile.load(smartSettingRepository)
        smartSettingLiveData.observe(lifecycleOwner, Observer<Set<SmartSetting>> { smartSettings ->

            val smartSettingViewDataList = smartSettings.withIndex().map { it ->
                smartSettingViewDataMap[it.index] = it.value
                SmartSettingViewData.getSmartSetting(it.index, it.value)
            }

            if (smartSettingViewDataList.isNotEmpty()) {
                smartSettingView.showSmartSettings(smartSettingViewDataList)
            } else {
                smartSettingView.showEmptyView()
            }
        })
    }

    override fun updateSmartSetting(smartSettingViewData: SmartSettingViewData) {
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