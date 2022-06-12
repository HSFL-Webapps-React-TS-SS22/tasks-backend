package de.progeek.tasks.web.model

data class CreateUserRequest(
    val username: String,
    val password: String,
    val profile: Map<String,String>? = emptyMap()
)
