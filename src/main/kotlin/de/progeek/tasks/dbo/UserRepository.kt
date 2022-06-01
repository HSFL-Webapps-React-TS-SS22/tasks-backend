package de.progeek.tasks.dbo

import de.progeek.tasks.model.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CoroutineCrudRepository<UserEntity, String> {
}