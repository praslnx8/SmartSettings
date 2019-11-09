package resources.db

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import response.SmartSettingSchema

class MongoDb  {

    val mongoClient = MongoClient(
        ServerAddress("localhost",27017),
        MongoCredential.createCredential(
            "smartuser",
            "smartsettings",
            "smarts3cr3t".toCharArray()
        ),
        MongoClientOptions.builder().codecRegistry(CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()))).build()
    )

    fun getSmartSettingSchemas() : List<SmartSettingSchema> {
        val dataBase = mongoClient.getDatabase("smartsettings")
        val collection = dataBase.getCollection("smartSettingSchema", SmartSettingSchema::class.java)

        return collection.find().toList()
    }

    fun insertSmartSettingSchema(smartSettingSchema: SmartSettingSchema) : Boolean {

        val dataBase = mongoClient.getDatabase("smartsettings")
        val collection = dataBase.getCollection("smartSettingSchema", SmartSettingSchema::class.java)

        collection.insertOne(smartSettingSchema)

        return true
    }
}