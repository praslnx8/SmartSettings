package com.smartsettings.ai.uiModules.smartSettingView

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.smartsettings.ai.core.SmartProfile
import com.smartsettings.ai.core.SmartSettingRepository
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.di.DependencyProvider
import java.lang.ref.WeakReference

class SmartSettingViewModel : ViewModel(), SmartSettingViewPresenter {

    val smartSettingRepository: SmartSettingRepository = DependencyProvider.smartSettingRepository

    private val smartSettingLiveData = SmartProfile.getSmartSettingLiveData()

    private lateinit var smartSettingViewReference: WeakReference<SmartSettingView>

    private val smartSettingList = mutableListOf<SmartSetting>()

    override fun setHomeView(smartSettingViewReference: WeakReference<SmartSettingView>) {
        this.smartSettingViewReference = smartSettingViewReference
    }

    override fun getSmartSettings(lifecycleOwner: LifecycleOwner) {
        SmartProfile.load(smartSettingRepository)
        smartSettingLiveData.observe(lifecycleOwner, Observer<Set<SmartSetting>> { smartSettings ->

            smartSettingList.clear()
            smartSettingList.addAll(smartSettings)

            val smartSettingViewDataList = mutableListOf<SmartSettingViewData>()
            for (smartSetting in smartSettings) {
                smartSettingViewDataList.add(SmartSettingViewData.getSmartSetting(smartSetting))
            }

            if (smartSettingViewDataList.isNotEmpty()) {
                smartSettingViewReference.get()?.showSmartSettings(smartSettingViewDataList)
            } else {
                smartSettingViewReference.get()?.showEmptyView()
            }
        })
    }

    override fun updateSmartSetting(smartSettingViewData: SmartSettingViewData) {
        for (smartSetting in smartSettingList) {
            if (smartSetting.id == smartSettingViewData.id) {
                smartSetting.setEnabled(smartSettingViewData.isEnabled)
                SmartProfile.updateSmartSetting(smartSettingRepository, smartSetting)
                return
            }
        }
    }

    override fun deleteSmartSetting(smartSettingViewData: SmartSettingViewData) {
        for (smartSetting in smartSettingList) {
            if (smartSetting.id == smartSettingViewData.id) {
                SmartProfile.deleteSmartSetting(smartSettingRepository, smartSetting)
                return
            }
        }
    }
}