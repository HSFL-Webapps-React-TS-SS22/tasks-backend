package de.progeek.tasks.dbo

import org.springframework.data.annotation.Id

data class UserEntity(
    @Id
    var username: String,
    var password: String,
    var profile: Map<String, String>? = emptyMap()
)