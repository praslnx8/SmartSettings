package com.smartsettings.ai.core.smartSettingCreator

import com.smartsettings.ai.core.contextListeners.ContextListenerType
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.di.DependencyProvider
import com.smartsettings.ai.resources.cloud.ApiService
import com.smartsettings.ai.resources.cloud.SmartSettingSchemaCloudData
import com.smartsettings.ai.resources.db.SmartSettingSchemaDBModel
import com.smartsettings.ai.resources.db.SmartSettingSchemaDao
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SmartSettingSchemaRepo {

    private val smartSettingSchemaDao: SmartSettingSchemaDao =
        DependencyProvider.smartSettingSchemaDao

    private val apiService: ApiService = DependencyProvider.apiService

    /**
     * Get schemas from db.
     * If first time or the db is empty. It will sync from cloud and return.
     */
    fun getSchemas(schemasCallback: (List<SmartSettingSchemaDBModel>) -> Unit) {
        loadSchemaFromDB {
            if (it.isEmpty()) {
                syncSchemaFromCloud(false) {
                    loadSchemaFromDB { updatedList ->
                        if (updatedList.isEmpty()) {
                            schemasCallback(getDefaultSettings())
                        } else {
                            schemasCallback(updatedList)
                        }
                    }
                }
            } else {
                schemasCallback(it)
            }
        }
    }

    private fun getDefaultSettings(): List<SmartSettingSchemaDBModel> {
        val smartSettingSchemaDBModels = ArrayList<SmartSettingSchemaDBModel>()
        smartSettingSchemaDBModels.add(
            SmartSettingSchemaDBModel(
                "Mute volume at location",
                null,
                listOf(SettingChangerType.VOLUME_MUTE_CHANGER.value),
                listOf(ContextListenerType.LOCATION_LISTENER.value),
                SmartSetting.AND
            )
        )

        return smartSettingSchemaDBModels
    }

    private fun loadSchemaFromDB(schemasCallback: (List<SmartSettingSchemaDBModel>) -> Unit) {
        doAsync {
            val smartSettingSchemas = smartSettingSchemaDao.getSmartSettingSchemas()

            uiThread {
                schemasCallback(smartSettingSchemas)
            }
        }
    }

    private fun persistSchema(
        isDeleteOld: Boolean,
        smartSettingSchemas: List<SmartSettingSchemaDBModel>,
        completeCallback: () -> Unit
    ) {

        doAsync {
            if(isDeleteOld) {
                smartSettingSchemaDao.deleteAll()
            }
            smartSettingSchemaDao.insertSmartSetting(smartSettingSchemas)

            uiThread {
                completeCallback()
            }
        }
    }

    private fun syncSchemaFromCloud(isDeleteOld : Boolean, completeCallback: () -> Unit) {
        apiService.getSchemas().enqueue(object : Callback<List<SmartSettingSchemaCloudData>> {
            override fun onFailure(call: Call<List<SmartSettingSchemaCloudData>>, t: Throwable) {
                completeCallback()
            }

            override fun onResponse(
                call: Call<List<SmartSettingSchemaCloudData>>,
                response: Response<List<SmartSettingSchemaCloudData>>
            ) {
                val smartSettingSchemaDbModels = ArrayList<SmartSettingSchemaDBModel>()

                for (smartSettingSchemaCloudData in response.body() ?: ArrayList()) {
                    smartSettingSchemaDbModels.add(
                        SmartSettingSchemaDBModel(
                            smartSettingSchemaCloudData.title,
                            smartSettingSchemaCloudData.description,
                            smartSettingSchemaCloudData.settingChangerSchemas,
                            smartSettingSchemaCloudData.contextListenerSchemas,
                            smartSettingSchemaCloudData.conjunctionLogic
                        )
                    )
                }

                if(smartSettingSchemaDbModels.isNotEmpty()) {
                    persistSchema(isDeleteOld, smartSettingSchemaDbModels) {
                        completeCallback()
                    }
                }
            }
        })
    }

    fun syncSchemaCompletely() {
        syncSchemaFromCloud(true){}
    }
}