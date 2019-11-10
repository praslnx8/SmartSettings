package resources.db

import ApplicationConfig
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

object MongoDb  {

    private const val DATABASE_NAME = "smartsettings"

    private val mongoClient = MongoClient(
        ServerAddress(ApplicationConfig.mongoDbHost,ApplicationConfig.mongoDbPort.toInt()),
        MongoCredential.createCredential(
            ApplicationConfig.mongoDbUserName,
            DATABASE_NAME,
            ApplicationConfig.mongoDbPassword.toCharArray()
        ),
        MongoClientOptions.builder().codecRegistry(CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()))).build()
    )

    private fun getDatabase(): MongoDatabase {
        return mongoClient.getDatabase(DATABASE_NAME)
    }

    fun <T> getCollections(collectionName:String, documentClass : Class<T>) : List<T>{
        val collection = getDatabase().getCollection(collectionName, documentClass)
        return collection.find().toList()
    }

    fun <T> insertData(collectionName: String, data : T, documentClass : Class<T>) : Boolean {
        val collection = getDatabase().getCollection(collectionName, documentClass)
        collection.insertOne(data)
        return true
    }
}