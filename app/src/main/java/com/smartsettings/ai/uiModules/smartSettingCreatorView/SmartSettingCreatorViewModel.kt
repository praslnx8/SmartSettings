package com.smartsettings.ai.uiModules.smartSettingCreatorView

import androidx.lifecycle.ViewModel
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.core.contextListeners.ContextListenerType
import com.smartsettings.ai.core.settingChangers.SettingChangerType
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingCreator
import com.smartsettings.ai.core.smartSettingCreator.SmartSettingCreatorCallback
import com.smartsettings.ai.core.smartSettings.SmartSetting
import com.smartsettings.ai.resources.db.SmartSettingSchemaDBModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class SmartSettingCreatorViewModel : ViewModel(), SmartSettingCreatorPresenter {

    @Inject
    lateinit var smartSettingCreator: SmartSettingCreator

    private lateinit var smartSettingCreatorViewReference: WeakReference<SmartSettingCreatorView>

    private val smartSettingSchemaDbModelMap: MutableMap<Int?, SmartSettingSchemaDBModel> = HashMap()

    init {
        SmartApp.appComponent.inject(this)
    }

    override fun getSmartSettingSchemas() {
        smartSettingCreatorViewReference.get()?.showLoading()
        smartSettingCreator.getSmartSettingSchemas {
            if (it.isNotEmpty()) {
                val smartSettingSchemaViewDataList = ArrayList<SmartSettingSchemaViewData>()
                smartSettingSchemaDbModelMap.clear()
                for (smartSettingSchemaDbModel in it) {
                    smartSettingSchemaDbModelMap[smartSettingSchemaDbModel.id] = smartSettingSchemaDbModel

                    smartSettingSchemaViewDataList.add(
                        SmartSettingSchemaViewData(
                            smartSettingSchemaDbModel.id ?: 0,
                            smartSettingSchemaDbModel.title,
                            smartSettingSchemaDbModel.description,
                            smartSettingSchemaDbModel.settingChangerSchemas,
                            smartSettingSchemaDbModel.contextListenerSchemas,
                            smartSettingSchemaDbModel.conjunctionLogic
                        )
                    )
                }
                smartSettingCreatorViewReference.get()?.showSmartSettingSchemas(smartSettingSchemaViewDataList)
            } else {
                smartSettingCreatorViewReference.get()?.showUnableToFetchInfo()
            }
        }
    }

    override fun parseSmartSettingSchema(smartSettingSchemaViewData: SmartSettingSchemaViewData) {
        val smartSettingSchemaDBModel = smartSettingSchemaDbModelMap[smartSettingSchemaViewData.id]
        smartSettingSchemaDBModel?.let {
            smartSettingCreator.parseSmartSettingSchema(it, object : SmartSettingCreatorCallback {
                override fun getContextListenerCriteriaData(
                    contextListenerTypes: List<ContextListenerType>,
                    criteriaDataCallback: (Map<ContextListenerType, Any>) -> Unit
                ) {
                    val map: MutableMap<ContextListenerType, Any> = mutableMapOf()

                    askViewToGetCriteriaData(map, contextListenerTypes.listIterator()) {
                        criteriaDataCallback(map)
                    }
                }

                override fun getSettingChangerActionData(
                    settingChangerTypes: List<SettingChangerType>,
                    actionDataCallback: (Map<SettingChangerType, Any>) -> Unit
                ) {
                    val map: MutableMap<SettingChangerType, Any> = mutableMapOf()

                    askViewToGetActionData(map, settingChangerTypes.listIterator()) {
                        actionDataCallback(map)
                    }
                }

                override fun getName(nameCallback: (String) -> Unit) {
                    smartSettingCreatorViewReference.get()?.askName {
                        nameCallback(it)
                    }
                }

                override fun onSmartSettingsCreated(smartSetting: SmartSetting) {
                    smartSettingCreatorViewReference.get()?.close()
                }
            })
        }
    }

    private fun askViewToGetCriteriaData(
        map: MutableMap<ContextListenerType, Any>,
        iterator: Iterator<ContextListenerType>,
        completeCallback: () -> Unit
    ) {
        if (iterator.hasNext()) {
            val contextListenerType = iterator.next()
            smartSettingCreatorViewReference.get()?.askCriteriaData(contextListenerType) {
                map[contextListenerType] = it
                askViewToGetCriteriaData(map, iterator, completeCallback)
            }
        } else {
            completeCallback()
        }
    }

    private fun askViewToGetActionData(
        map: MutableMap<SettingChangerType, Any>,
        iterator: Iterator<SettingChangerType>,
        completeCallback: () -> Unit
    ) {
        if (iterator.hasNext()) {
            val contextListenerType = iterator.next()
            smartSettingCreatorViewReference.get()?.askActionData(contextListenerType) {
                map[contextListenerType] = it
                askViewToGetActionData(map, iterator, completeCallback)
            }
        } else {
            completeCallback()
        }
    }

    override fun setView(smartSettingCreatorViewReference: WeakReference<SmartSettingCreatorView>) {
        this.smartSettingCreatorViewReference = smartSettingCreatorViewReference
    }

}