package de.progeek.tasks.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

class AuthenticationManager(private final val userDetailsService: UserDetailsService, val passwordEncoder: PasswordEncoder) : ReactiveAuthenticationManager {

    private val repositoryUserAuthenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService).apply {
        setPasswordEncoder(passwordEncoder)
    }


    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        if(authentication is UsernamePasswordAuthenticationToken) {
            return repositoryUserAuthenticationManager.authenticate(authentication)
        }

        throw RuntimeException("Unknown authentication: ${authentication?.javaClass}")
    }
}