package de.progeek.tasks.security

import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class JwtTokenFilter(val jwtTokenProvider: JwtTokenProvider, val userDetailsService: UserDetailsService): WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()

        if(!authHeader.isNullOrEmpty() && authHeader.startsWith("Bearer")) {
            val (_, token) = authHeader.split(' ')

            if(jwtTokenProvider.validateToken(token)) {
                val username = jwtTokenProvider.getUsername(token)

                val authentication = userDetailsService.findByUsername(username).map {
                    UsernamePasswordAuthenticationToken(it, null, listOf())
                }

                return authentication.flatMap {
                    chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(it))
                }
            }
        }

        return chain.filter(exchange)
    }

}

