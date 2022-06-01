package de.progeek.tasks.service

import de.progeek.tasks.model.AuthenticatedUser
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserDetailsService(val userService: UserService): ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return mono {
            username?.let {
                userService.findById(it)?.let {
                    AuthenticatedUser(it)
                }
            }
        }
    }
}