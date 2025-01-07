package com.tweet.uala.infrastructure.adapters.repository

import com.mongodb.ErrorCategory
import com.mongodb.MongoWriteException
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.Updates
import com.tweet.uala.application.exception.DuplicateException
import com.tweet.uala.application.exception.NotFoundException
import com.tweet.uala.domain.model.User
import com.tweet.uala.domain.port.UserRepository
import com.tweet.uala.infrastructure.adapters.repository.entity.UserEntity
import com.tweet.uala.infrastructure.adapters.repository.mapper.UserMapper
import jakarta.inject.Singleton
import org.bson.Document
import org.bson.types.ObjectId

@Singleton
class UserRepositoryImp(
    private val database: MongoDatabase,
    private val userMapper: UserMapper
) : UserRepository {

    private val collection: MongoCollection<UserEntity> = database.getCollection("users", UserEntity::class.java)

    init {
        createUniqueUsernameIndex()
    }

    private fun createUniqueUsernameIndex() {
        val index = Indexes.ascending("username")
        collection.createIndex(index, com.mongodb.client.model.IndexOptions().unique(true))
    }

    override fun save(user: User): User {
        val entity = userMapper.toEntity(user)

        try {
            collection.replaceOne(
                Document("_id", entity.id),
                entity,
                ReplaceOptions().upsert(true)
            )
            return user
        } catch (e: MongoWriteException) {
            if (e.error.category == ErrorCategory.DUPLICATE_KEY) {
                throw DuplicateException("Duplicate key error: username '${user.username}' already exists.")
            }
            throw e
        }
    }

    override fun findById(id: String): User? {
        val entity = collection.find(Filters.eq("_id", id)).firstOrNull()

        return entity?.let(userMapper::toDomain)
    }

    override fun saveFollowing(followerId: String, followingId: String): Boolean {
        val updateResult = collection.updateOne(
            Filters.eq("_id", followerId),
            Updates.addToSet("following", followingId)
        )
        return updateResult.modifiedCount > 0
    }

    override fun update(user: User): User {
        val userEntity = userMapper.toEntity(user)

        val filter = Filters.eq("_id", ObjectId(user.id))

        val update = Updates.combine(
            Updates.set("name", userEntity.name),
            Updates.set("username", userEntity.username),
            Updates.set("following", userEntity.following)
        )

        val result = collection.updateOne(filter, update)

        if (result.matchedCount == 0L) {
            throw NotFoundException("User with ID ${user.id} not found")
        }

        val updatedDocument = collection.find(filter).first()
            ?: throw NotFoundException("User with ID ${user.id} not found after update")

        return userMapper.toDomain(updatedDocument)
    }

    fun findByUsername(username: String): User? {
        val entity = collection.find(Document("username", username)).firstOrNull()
        return entity?.let { userMapper.toDomain(it) }
    }

    fun followUser(userId: String, followUserId: String): Boolean {
        val result = collection.updateOne(
            Document("_id", userId),
            Document("\$addToSet", Document("following", followUserId))
        )
        return result.modifiedCount > 0
    }

    fun unfollowUser(userId: String, unfollowUserId: String): Boolean {
        val result = collection.updateOne(
            Document("_id", userId),
            Document("\$pull", Document("following", unfollowUserId))
        )
        return result.modifiedCount > 0
    }
}
