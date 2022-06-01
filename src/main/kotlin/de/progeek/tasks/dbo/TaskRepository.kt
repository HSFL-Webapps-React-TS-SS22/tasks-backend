package de.progeek.tasks.dbo

import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : CoroutineCrudRepository<TaskEntity, ObjectId> {
    fun findAllByUser(user: String): Flow<TaskEntity>
    suspend fun findOneByUserAndId(user: String, id: ObjectId): TaskEntity?
}