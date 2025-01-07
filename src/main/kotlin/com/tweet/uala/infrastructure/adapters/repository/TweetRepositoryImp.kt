package com.tweet.uala.infrastructure.adapters.repository
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.tweet.uala.domain.model.Paginated
import com.tweet.uala.domain.model.Tweet
import com.tweet.uala.domain.port.TweetRepository
import com.tweet.uala.infrastructure.adapters.repository.entity.TweetEntity
import com.tweet.uala.infrastructure.adapters.repository.mapper.TweetMapper
import jakarta.inject.Singleton
import org.bson.Document
import java.time.Instant
import java.time.temporal.ChronoUnit

@Singleton
class TweetRepositoryImp(
    private val database: MongoDatabase,
    private val tweetMapper: TweetMapper
) : TweetRepository {

    companion object {
        const val LIMIT_DAY = 6L
        const val DESCENDING = -1
        const val ASCENDING = 1
    }

    private val collection: MongoCollection<TweetEntity> = database.getCollection("tweets", TweetEntity::class.java)

    override fun save(tweet: Tweet): Tweet {
        val entity = tweetMapper.toEntity(tweet)
        collection.insertOne(entity)
        return tweet
    }

    override fun findByUserIds(userIds: List<String>): List<Tweet> {
        val query = Document("userId", Document("\$in", userIds))

        return collection.find(query)
            .sort(Document("createdAt", -1))
            .map(tweetMapper::toDomain)
            .toList()
    }

    override fun findTimelinePaginated(
        userIds: List<String>,
        currentDate: Instant
    ): Paginated<Tweet> {
        val startOfPeriod = currentDate.minus(LIMIT_DAY, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)

        val query = Document("userId", Document("\$in", userIds))
            .append("createdAt", Document("\$gte", startOfPeriod).append("\$lte", currentDate))

        val tweets = collection.find(query)
            .sort(Document("createdAt", DESCENDING))
            .map { tweetMapper.toDomain(it) }
            .toList()

        val lastTweetDate = getLastTweetDate(userIds)
        val isLastPage = lastTweetDate == null || startOfPeriod.isBefore(lastTweetDate)

        return Paginated(
            items = tweets,
            nextPageDate = if (!isLastPage) startOfPeriod else null,
            isLastPage = isLastPage
        )
    }

    private fun getLastTweetDate(userIds: List<String>): Instant? {
        val query = Document("userId", Document("\$in", userIds))
        val oldestTweetDocument = collection.find(query)
            .sort(Document("createdAt", ASCENDING))
            .firstOrNull()

        return oldestTweetDocument?.createdAt
    }
}
