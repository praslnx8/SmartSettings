package com.smartsettings.ai.uiModules.smartSettingSchemaChooserView

import androidx.lifecycle.ViewModel
import com.smartsettings.ai.core.smartSettingSchemaProvider.SmartSettingSchemaProvider
import com.smartsettings.ai.di.DependencyProvider
import com.smartsettings.ai.resources.db.SmartSettingSchemaDBModel
import java.lang.ref.WeakReference

class SmartSettingSchemaListViewModel : ViewModel(),
    SmartSettingSchemaPresenter {

    private val smartSettingSchemaProvider: SmartSettingSchemaProvider = DependencyProvider.smartsettingSchemaProvider

    private lateinit var smartSettingSchemaViewReference: WeakReference<SmartSettingSchemaView>

    private val smartSettingSchemaDbModelMap: MutableMap<String?, SmartSettingSchemaDBModel> =
        HashMap()

    override fun getSmartSettingSchemas() {
        smartSettingSchemaViewReference.get()?.showLoading()
        smartSettingSchemaProvider.getSmartSettingSchemas {
            if (it.isNotEmpty()) {
                val smartSettingSchemaViewDataList = ArrayList<SmartSettingSchemaViewData>()
                smartSettingSchemaDbModelMap.clear()
                for (smartSettingSchemaDbModel in it) {
                    smartSettingSchemaDbModelMap[smartSettingSchemaDbModel.title] =
                        smartSettingSchemaDbModel

                    smartSettingSchemaViewDataList.add(
                        SmartSettingSchemaViewData(
                            smartSettingSchemaDbModel.title,
                            smartSettingSchemaDbModel.description,
                            smartSettingSchemaDbModel.settingChangerSchemas.asSequence().map { data ->
                                SettingChangerSchemaViewData(
                                    data.type,
                                    data.input
                                )
                            }.toList(),
                            smartSettingSchemaDbModel.contextListenerSchemas.asSequence().map { data ->
                                ContextListenerSchemaViewData(
                                    data.type,
                                    data.input
                                )
                            }.toList(),
                            smartSettingSchemaDbModel.conjunctionLogic
                        )
                    )
                }
                smartSettingSchemaViewReference.get()
                    ?.showSmartSettingSchemas(smartSettingSchemaViewDataList)
            } else {
                smartSettingSchemaViewReference.get()?.showUnableToFetchInfo()
            }
        }
    }


    override fun setView(smartSettingSchemaViewReference: WeakReference<SmartSettingSchemaView>) {
        this.smartSettingSchemaViewReference = smartSettingSchemaViewReference
    }

}