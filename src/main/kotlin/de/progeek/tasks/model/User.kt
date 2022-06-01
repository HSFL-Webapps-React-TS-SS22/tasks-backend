package de.progeek.tasks.model

data class User(
    val username: String,
    @Transient
    val password: String
)
