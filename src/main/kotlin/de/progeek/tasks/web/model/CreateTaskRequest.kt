package de.progeek.tasks.web.model

data class CreateTaskRequest(
    val title: String,
    val complete: Boolean = false,
    val description: String? = null,
    val tags: List<String> = listOf(),
    val metadata: Map<String, String> = mapOf()
)
