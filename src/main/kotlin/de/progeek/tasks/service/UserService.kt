package de.progeek.tasks.service

import de.progeek.tasks.dbo.UserRepository
import de.progeek.tasks.mapper.toDTO
import de.progeek.tasks.mapper.toEntity
import de.progeek.tasks.model.User
import de.progeek.tasks.security.AuthenticatedUser
import de.progeek.tasks.security.JwtTokenProvider
import de.progeek.tasks.web.model.CreateUserRequest
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder, val jwtTokenProvider: JwtTokenProvider, val authenticationManager: ReactiveAuthenticationManager) {
    object UserAlreadyExistsException: UserServiceException("User already exists")

    suspend fun login(username: String?, password: String?): String? {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                username,
                password
        )).doOnError { e ->throw RuntimeException(
                "Invalid username/password supplied: ${e.localizedMessage}"
        ) }.awaitSingle()

        return jwtTokenProvider.createToken(username)
    }

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

    suspend fun getCurrentUser(): User {
        val auth = ReactiveSecurityContextHolder.getContext().awaitSingle().authentication
        return (auth.principal as AuthenticatedUser).user
    }
}