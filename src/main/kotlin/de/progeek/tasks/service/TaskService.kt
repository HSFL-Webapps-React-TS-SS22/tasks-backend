package de.progeek.tasks.service

import de.progeek.tasks.dbo.TaskEntity
import de.progeek.tasks.dbo.TaskRepository
import de.progeek.tasks.mapper.ObjectIdMapper
import de.progeek.tasks.mapper.TaskMapper
import de.progeek.tasks.model.Task
import de.progeek.tasks.security.AuthenticatedUser
import de.progeek.tasks.web.model.CreateTaskRequest
import de.progeek.tasks.web.model.UpdateTaskRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class TaskService(
    @Lazy val repository: TaskRepository,
    @Lazy val mapper: TaskMapper,
    @Lazy val objectIdMapper: ObjectIdMapper
) {

    suspend fun getAll(): Flow<Task> {
        return repository.findAllByUser(getUser()).map { it.toDto() }
    }

    suspend fun findById(id: String): Task? {
        return repository.findOneByUserAndId(getUser(), objectIdMapper.map(id))?.toDto()
    }

    suspend fun create(createTaskRequest: CreateTaskRequest): Task {
        val entity = mapper.toEntity(createTaskRequest, getUser())
        return repository.save(entity).let {
            it.toDto()
        }
    }

    suspend fun update(id: String, updateTaskRequest: UpdateTaskRequest): Task? {
        return repository.findOneByUserAndId(getUser(), objectIdMapper.map(id))?.let { entity ->
            repository.save(mapper.toEntity(updateTaskRequest, entity)).toDto()
        }
    }

    suspend fun delete(id: String): Task? {
        return repository.findOneByUserAndId(getUser(), objectIdMapper.map(id))?.let { entity ->
            repository.delete(entity)
            entity.toDto()
        }
    }

    suspend private fun getUser(): String {
        val auth = ReactiveSecurityContextHolder.getContext().awaitSingle().authentication
        return (auth.principal as AuthenticatedUser).username
    }

    fun TaskEntity.toDto() = mapper.toDTO(this)
    fun Task.toEntity() = mapper.toEntity(this)
}