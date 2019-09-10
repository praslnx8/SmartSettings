package com.smartsettings.ai.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.resources.db.SmartSettingDBModel
import com.smartsettings.ai.resources.db.SmartSettingDao
import javax.inject.Inject

class SmartSettingRepository {

    @Inject
    lateinit var smartSettingDao: SmartSettingDao

    init {
        SmartApp.appComponent.inject(this)
    }

    val smartSettings: LiveData<List<SmartSettingDBModel>> = smartSettingDao.getSmartSettings()

    @WorkerThread
    fun insert(smartSettingDBModel: SmartSettingDBModel) {
        smartSettingDao.insertSmartSetting(smartSettingDBModel)
    }
}