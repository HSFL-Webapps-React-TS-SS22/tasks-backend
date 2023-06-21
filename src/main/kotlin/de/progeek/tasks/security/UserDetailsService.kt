package de.progeek.tasks.security

import de.progeek.tasks.dbo.UserRepository
import de.progeek.tasks.mapper.toDTO
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserDetailsService(val userRepository: UserRepository): ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return mono {
            username?.let { username ->
                userRepository.findById(username)?.let {
                    AuthenticatedUser(it.toDTO())
                }
            }
        }
    }
}