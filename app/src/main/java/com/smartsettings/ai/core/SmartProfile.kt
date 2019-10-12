package com.smartsettings.ai.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartsettings.ai.core.smartSettings.SmartSetting

object SmartProfile {

    private val smartSettings = HashSet<SmartSetting<out Any>>()

    private val smartSettingsLiveData: MutableLiveData<Set<SmartSetting<out Any>>> = MutableLiveData(
        smartSettings
    )

    private val smartSettingsListLiveData: MutableLiveData<Set<SmartSetting<out Any>>> = MutableLiveData(
        smartSettings
    )

    private var isLoaded = false

    private val smartSettingChangeCallback: ((SmartSetting<out Any>) -> Unit) = {
        smartSettings.add(it)
        smartSettingsLiveData.postValue(smartSettings)
    }

    private fun updateLiveData() {
        smartSettingsLiveData.value = smartSettings
        smartSettingsListLiveData.value = smartSettings
    }

    fun load(smartSettingRepository: SmartSettingRepository) {

        if (isLoaded) {
            updateLiveData()
        } else {
            smartSettingRepository.getSmartSettings {
                for (smartSetting in it) {
                    smartSetting.setChangesCallback(smartSettingChangeCallback)
                    smartSettings.add(smartSetting)
                }
                updateLiveData()
                isLoaded = true
            }
        }
    }

    fun getSmartSettingLiveData(): LiveData<Set<SmartSetting<out Any>>> {
        return smartSettingsLiveData
    }

    fun getSmartSettingListLiveData(): LiveData<Set<SmartSetting<out Any>>> {
        return smartSettingsListLiveData
    }

    fun addSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any>
    ) {
        checkAndAdd(smartSetting)
        smartSettingRepository.addSmartSetting(smartSetting)
        updateLiveData()
    }

    fun updateSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any>
    ) {
        checkAndAdd(smartSetting)
        smartSettingRepository.updateSmartSetting(smartSetting)
        updateLiveData()
    }

    fun deleteSmartSetting(
        smartSettingRepository: SmartSettingRepository,
        smartSetting: SmartSetting<out Any>
    ) {
        smartSettings.remove(smartSetting)
        smartSettingRepository.deleteSmartSetting(smartSetting)
        updateLiveData()
    }

    private fun checkAndAdd(
        smartSetting: SmartSetting<out Any>
    ) {
        if (!smartSettings.contains(smartSetting)) {
            smartSetting.setChangesCallback(smartSettingChangeCallback)
            smartSettings.add(smartSetting)
        }
    }
}