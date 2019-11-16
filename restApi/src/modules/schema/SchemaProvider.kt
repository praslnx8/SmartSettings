package modules.schema

import cloud.SmartSettingSchemaCloudData
import resources.db.MongoDb

class SmartSettingSchemaRepo {

    private val collectionSmartSettingSchema = "smartSettingSchema"

    fun getSmartSettingSchemas() : List<SmartSettingSchemaCloudData> {
        return MongoDb.getCollections(collectionSmartSettingSchema, SmartSettingSchemaCloudData::class.java)
    }

    fun insertSmartSettingSchema(smartSettingSchema: SmartSettingSchemaCloudData) : Boolean {
        return MongoDb.insertData(collectionSmartSettingSchema, smartSettingSchema, SmartSettingSchemaCloudData::class.java)
    }
}