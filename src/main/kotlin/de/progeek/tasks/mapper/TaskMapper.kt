package de.progeek.tasks.mapper

import de.progeek.tasks.dbo.TaskEntity
import de.progeek.tasks.model.Task
import de.progeek.tasks.web.model.CreateTaskRequest
import de.progeek.tasks.web.model.UpdateTaskRequest
import org.bson.types.ObjectId
import org.mapstruct.*

@Mapper(uses = [ObjectIdMapper::class])
interface TaskMapper {
    fun toDTO(entity: TaskEntity): Task
    fun toEntity(task: Task): TaskEntity

    @Mappings(
        Mapping(target = "id", expression = "java(new ObjectId())")
    )
    fun toEntity(createTaskRequest: CreateTaskRequest, user: String): TaskEntity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun toEntity(updateTaskRequest: UpdateTaskRequest, @MappingTarget entity: TaskEntity): TaskEntity
}