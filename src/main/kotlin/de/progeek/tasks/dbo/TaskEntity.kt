package de.progeek.tasks.dbo

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

data class TaskEntity(
    @Id
    var id: ObjectId,
    var user: String,
    var title: String,
    var complete: Boolean = false,
    var description: String? = null,
    var tags: List<String> = listOf(),
    var metadata: Map<String, String> = mapOf()
)
