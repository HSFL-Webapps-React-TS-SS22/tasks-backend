package de.progeek.tasks.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class User(
    val username: String,
    @JsonIgnore
    val password: String,
    val profile: Map<String, String> = emptyMap()
)
