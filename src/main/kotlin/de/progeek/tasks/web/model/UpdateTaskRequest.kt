package de.progeek.tasks.web.model

data class UpdateTaskRequest(
    val title: String? = null,
    val complete: Boolean? = null,
    val description: String? = null,
    val tags: List<String>? = null,
    val metadata: Map<String, String>? = null
)
