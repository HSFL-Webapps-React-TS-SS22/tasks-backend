package de.progeek.tasks.mapper

import de.progeek.tasks.dbo.UserEntity
import de.progeek.tasks.model.User
import de.progeek.tasks.web.model.CreateUserRequest

fun UserEntity.toDTO(): User {
    return User(
        username = this.username,
        password = this.password,
        profile = this.profile ?: emptyMap()
    )
}

fun CreateUserRequest.toEntity(): UserEntity {
    return UserEntity(
        username = this.username,
        password = this.password,
        profile = this.profile
    )
}