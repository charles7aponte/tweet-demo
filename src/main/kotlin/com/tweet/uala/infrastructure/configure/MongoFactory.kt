package com.tweet.uala.infrastructure.configure

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider

@Factory
class MongoFactory {

    @Value("\${mongodb.uri}")
    lateinit var connectionString: String

    @Value("\${mongodb.database}")
    lateinit var dbName: String

    @Singleton
    fun mongoDatabase(): MongoDatabase {
        val codecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        )

        val client: MongoClient = MongoClients.create(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(connectionString))
                .codecRegistry(codecRegistry)
                .build()
        )
        return client.getDatabase(dbName)
    }
}
