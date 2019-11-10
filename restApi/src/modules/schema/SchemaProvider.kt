package modules.schema

import resources.db.MongoDb
import response.SmartSettingSchema

class SmartSettingSchemaRepo {

    private val collectionSmartSettingSchema = "smartSettingSchema"

    fun getSmartSettingSchemas() : List<SmartSettingSchema> {
        return MongoDb.getCollections(collectionSmartSettingSchema, SmartSettingSchema::class.java)
    }

    fun insertSmartSettingSchema(smartSettingSchema: SmartSettingSchema) : Boolean {
        return MongoDb.insertData(collectionSmartSettingSchema, smartSettingSchema, SmartSettingSchema::class.java)
    }
}