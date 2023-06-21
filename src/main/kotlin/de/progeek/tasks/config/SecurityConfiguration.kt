package de.progeek.tasks.config

import de.progeek.tasks.security.AuthenticationManager
import de.progeek.tasks.security.JwtTokenFilter
import de.progeek.tasks.security.UserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration(val userDetailsService: UserDetailsService, val jwtTokenFilter: JwtTokenFilter)  {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager {
        return AuthenticationManager(userDetailsService, passwordEncoder())
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/task").authenticated()
                .pathMatchers(HttpMethod.GET, "/user").authenticated()
                .pathMatchers("/**").permitAll()
                .and()
                .cors().configurationSource(createCorsConfigSource())
                .and()
                .httpBasic()
                .and()
                .formLogin().disable()
                .authenticationManager(authenticationManager())
                .addFilterBefore(jwtTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    fun createCorsConfigSource(): CorsConfigurationSource {
        val corsConfig = CorsConfiguration()
        corsConfig.applyPermitDefaultValues()
        corsConfig.allowedOriginPatterns = listOf("*")
        corsConfig.addAllowedMethod(HttpMethod.GET)
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS)
        corsConfig.addAllowedMethod(HttpMethod.DELETE)
        corsConfig.maxAge = 8000L
        corsConfig.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)

        return source
    }
}