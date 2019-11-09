package resources.db

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import response.SmartSettingSchema

class MongoDb  {

    val mongoClient = MongoClient(
        ServerAddress("localhost",27017),
        MongoCredential.createCredential(
            "dev-admin",
            "dev",
            "S3cuRE!".toCharArray()
        ),
        MongoClientOptions.builder().build()
    )

    fun getSmartSettingSchemas() : List<SmartSettingSchema> {
        val dataBase = mongoClient.getDatabase("smartsettings")
        val collection = dataBase.getCollection("smartSettingSchema", SmartSettingSchema::class.java)

        return collection.find().toList()
    }
}