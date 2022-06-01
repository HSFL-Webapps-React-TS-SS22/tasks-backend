package de.progeek.tasks.web

import de.progeek.tasks.model.User
import de.progeek.tasks.service.UserService
import de.progeek.tasks.web.model.CreateUserRequest
import org.springframework.context.annotation.Lazy
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/user"], produces = ["application/json"])
class UserController(@Lazy val userService: UserService) {
    @PostMapping
    suspend fun create(@RequestBody createUserRequest: CreateUserRequest): User {
        return userService.createUser(createUserRequest)
    }

}