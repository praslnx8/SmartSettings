package resources.db

import ApplicationConfig
import cloud.ContextListenerCloudData
import cloud.SettingChangerCloudData
import cloud.SmartSettingSchemaCloudData
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import core.ContextListenerType
import core.SettingChangerType
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

object MongoDb {

    private const val DATABASE_NAME = "smartsettings"

    private val mongoClient = MongoClient(
        ServerAddress(ApplicationConfig.mongoDbHost, ApplicationConfig.mongoDbPort.toInt()),
        MongoCredential.createCredential(
            ApplicationConfig.mongoDbUserName,
            DATABASE_NAME,
            ApplicationConfig.mongoDbPassword.toCharArray()
        ),


        MongoClientOptions.builder().codecRegistry(
            CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(
                    ResponseCodec()
                ),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            )
        ).build()
    )

    private fun getDatabase(): MongoDatabase {
        return mongoClient.getDatabase(DATABASE_NAME)
    }

    fun <T> getCollections(collectionName: String, documentClass: Class<T>): List<T> {
        val collection = getDatabase().getCollection(collectionName, documentClass)
        return collection.find().toList()
    }

    fun <T> insertData(collectionName: String, data: T, documentClass: Class<T>): Boolean {
        val collection = getDatabase().getCollection(collectionName, documentClass)
        collection.insertOne(data)
        return true
    }
}


class ResponseCodec : Codec<SmartSettingSchemaCloudData> {
    override fun getEncoderClass(): Class<SmartSettingSchemaCloudData> {
        return SmartSettingSchemaCloudData::class.java
    }

    override fun encode(
        writer: BsonWriter?,
        value: SmartSettingSchemaCloudData?,
        encoderContext: EncoderContext?
    ) {
        writer?.writeStartDocument()
        writer?.writeString("title", value?.title)
        writer?.writeString("description", value?.description)
        writer?.writeString("conjunctionLogic", value?.conjunctionLogic)
        writer?.writeStartArray("contextListenerSchemas")
        for (contextListenerSchema in value?.contextListenerSchemas ?: listOf()) {
            writer?.writeStartDocument()
            writer?.writeString("type", contextListenerSchema.type.name)
            writer?.writeString("input", contextListenerSchema.input ?: "")
            writer?.writeEndDocument()
        }
        writer?.writeEndArray()
        writer?.writeStartArray("settingChangerSchemas")
        for (settingChangerSchema in value?.settingChangerSchemas ?: listOf()) {
            writer?.writeStartDocument()
            writer?.writeString("type", settingChangerSchema.type.name)
            writer?.writeString("input", settingChangerSchema.input ?: "")
            writer?.writeEndDocument()
        }
        writer?.writeEndArray()
        writer?.writeEndDocument()
    }

    override fun decode(
        reader: BsonReader?,
        decoderContext: DecoderContext?
    ): SmartSettingSchemaCloudData {

        reader?.readStartDocument()
        val id = reader?.readObjectId().toString()
        val title = reader?.readString("title") ?: ""
        val description = reader?.readString("description")
        val conjunctionLogic = reader?.readString("conjunctionLogic") ?: ""
        val contextListenerSchemas = mutableListOf<ContextListenerCloudData>()
        reader?.readStartArray()
        while (reader?.currentBsonType == BsonType.ARRAY) {
            reader.readStartDocument()
            val contextListenerType = ContextListenerType.valueOf(reader.readString("type"))
            val input = reader.readString("input")
            contextListenerSchemas.add(ContextListenerCloudData(contextListenerType, if(input.isNotBlank()) input else null))
            reader.readEndDocument()
        }
        reader?.readEndArray()
        val settingChangerSchemas = mutableListOf<SettingChangerCloudData>()
        reader?.readStartArray()
        while (reader?.currentBsonType == BsonType.ARRAY) {
            reader.readStartDocument()
            val settingChangerType = SettingChangerType.valueOf(reader.readString("type"))
            val input = reader.readString("input")
            settingChangerSchemas.add(SettingChangerCloudData(settingChangerType, if(input.isNotBlank()) input else null))
            reader.readEndDocument()
        }
        reader?.readEndArray()
        reader?.readEndDocument()

        return SmartSettingSchemaCloudData(
            id,
            title,
            description,
            settingChangerSchemas,
            contextListenerSchemas,
            conjunctionLogic
        )
    }
}

