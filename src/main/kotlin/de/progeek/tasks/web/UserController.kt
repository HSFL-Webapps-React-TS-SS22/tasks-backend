package de.progeek.tasks.web

import de.progeek.tasks.model.User
import de.progeek.tasks.service.UserService
import de.progeek.tasks.web.model.AuthRequest
import de.progeek.tasks.web.model.CreateUserRequest
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/user"], produces = ["application/json"])
class UserController(@Lazy val userService: UserService) {
    @GetMapping
    suspend fun get(): User {
        return userService.getCurrentUser()
    }

    @PostMapping
    suspend fun create(@RequestBody createUserRequest: CreateUserRequest): User {
        return userService.createUser(createUserRequest)
    }

    @PostMapping("/auth", produces = ["text/plain"])
    suspend fun auth(@RequestBody authRequest: AuthRequest): String {
        return userService.login(authRequest.username, authRequest.password) ?: ""
    }
}