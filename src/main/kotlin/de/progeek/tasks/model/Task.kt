package de.progeek.tasks.model

data class Task(
    val id: String,
    val title: String,
    val complete: Boolean = false,
    val description: String? = null,
    val tags: List<String> = listOf(),
    val metadata: Map<String, String> = mapOf()
)
