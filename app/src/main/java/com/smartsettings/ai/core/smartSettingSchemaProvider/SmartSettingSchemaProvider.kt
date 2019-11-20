package com.smartsettings.ai.core.smartSettingSchemaProvider

import com.smartsettings.ai.di.DependencyProvider
import com.smartsettings.ai.resources.db.SmartSettingSchemaDBModel

object SmartSettingSchemaProvider {

    private val smartSettingSchemaRepo: SmartSettingSchemaRepo =
        DependencyProvider.smartSettingSchemaRepo

    fun getSmartSettingSchemas(schemasCallback: (List<SmartSettingSchemaDBModel>) -> Unit) {
        smartSettingSchemaRepo.getSchemas {
            schemasCallback(it)
        }
    }

    fun syncSmartSettingSchema() {
        smartSettingSchemaRepo.syncSchemaCompletely()
    }

    fun getSmartSettingSchema(
        schemaId: String,
        schemaCallback: (SmartSettingSchemaDBModel?) -> Unit
    ) {
        smartSettingSchemaRepo.getSchemaById(schemaId) {
            schemaCallback(it)
        }
    }
}