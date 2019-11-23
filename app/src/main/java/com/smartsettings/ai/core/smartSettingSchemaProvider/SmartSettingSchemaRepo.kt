package com.smartsettings.ai.core.smartSettingSchemaProvider

import cloud.ContextListenerCloudData
import cloud.SettingChangerCloudData
import cloud.SmartSettingSchemaCloudData
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.di.DependencyProvider
import com.smartsettings.ai.resources.cloud.ApiService
import com.smartsettings.ai.resources.db.ContextListenerSchemaDBModel
import com.smartsettings.ai.resources.db.SettingChangerSchemaDBModel
import com.smartsettings.ai.resources.db.SmartSettingSchemaDBModel
import com.smartsettings.ai.resources.db.SmartSettingSchemaDao
import core.ContextListenerType
import core.SettingChangerType
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
                "",
                "Mute volume at location",
                "",
                listOf(SettingChangerSchemaDBModel(SettingChangerType.VOLUME_CHANGER, "", null)),
                listOf(ContextListenerSchemaDBModel(ContextListenerType.LOCATION_LISTENER, "", null)),
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
            if (isDeleteOld) {
                smartSettingSchemaDao.deleteAll()
            }
            smartSettingSchemaDao.insertSmartSetting(smartSettingSchemas)

            uiThread {
                completeCallback()
            }
        }
    }

    private fun syncSchemaFromCloud(isDeleteOld: Boolean, completeCallback: () -> Unit) {
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
                            smartSettingSchemaCloudData.id ?: "",
                            smartSettingSchemaCloudData.title,
                            smartSettingSchemaCloudData.description,
                            convertSettingChangerSchemaToDBModel(smartSettingSchemaCloudData.settingChangerSchemas),
                            convertContextListenerSchemaToDBModel(smartSettingSchemaCloudData.contextListenerSchemas),
                            smartSettingSchemaCloudData.conjunctionLogic
                        )
                    )
                }

                if (smartSettingSchemaDbModels.isNotEmpty()) {
                    persistSchema(isDeleteOld, smartSettingSchemaDbModels) {
                        completeCallback()
                    }
                }
            }
        })
    }

    private fun convertSettingChangerSchemaToDBModel(settingChangerSchemas: List<SettingChangerCloudData>): List<SettingChangerSchemaDBModel> {
        return settingChangerSchemas.asSequence()
            .map { data -> SettingChangerSchemaDBModel(data.type, data.description, data.input) }.toList()
    }

    private fun convertContextListenerSchemaToDBModel(contextListenerSchemas: List<ContextListenerCloudData>): List<ContextListenerSchemaDBModel> {
        return contextListenerSchemas.asSequence()
            .map { data -> ContextListenerSchemaDBModel(data.type, data.description, data.input) }.toList()
    }

    fun syncSchemaCompletely() {
        syncSchemaFromCloud(true) {}
    }

    fun getSchemaById(schemaId: String, schemaCallback: (SmartSettingSchemaDBModel?) -> Unit) {
        doAsync {
            val smartSettingSchemaDBModel = smartSettingSchemaDao.getSmartSettingSchemaById(schemaId)

            uiThread {
                schemaCallback(smartSettingSchemaDBModel)
            }
        }
    }
}