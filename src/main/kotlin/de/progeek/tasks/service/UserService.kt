package de.progeek.tasks.service

import de.progeek.tasks.dbo.UserRepository
import de.progeek.tasks.mapper.toDTO
import de.progeek.tasks.mapper.toEntity
import de.progeek.tasks.model.User
import de.progeek.tasks.web.model.CreateUserRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder) {
    object UserAlreadyExistsException: UserServiceException("User already exists")

    suspend fun createUser(createUserRequest: CreateUserRequest): User {
        userRepository.findById(createUserRequest.username)?.let {
            throw UserAlreadyExistsException
        }

        val entity = createUserRequest.toEntity()
        entity.password = passwordEncoder.encode(entity.password)
        return userRepository.save(entity).toDTO()
    }

    suspend fun findById(id: String): User? {
        return userRepository.findById(id)?.let { it.toDTO() }
    }
}