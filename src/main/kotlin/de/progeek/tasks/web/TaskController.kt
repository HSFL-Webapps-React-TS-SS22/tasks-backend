package de.progeek.tasks.web

import de.progeek.tasks.dbo.TaskEntity
import de.progeek.tasks.model.Task
import de.progeek.tasks.service.TaskService
import de.progeek.tasks.web.model.CreateTaskRequest
import de.progeek.tasks.web.model.UpdateTaskRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import kotlinx.coroutines.flow.Flow
import org.springframework.context.annotation.Lazy
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/task"], produces = ["application/json"])
class TaskController(@Lazy val taskService: TaskService) {

    @GetMapping
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = Array<Task>::class)
    )
    suspend fun getAll(): Flow<Task> {
        return taskService.getAll()
    }

    @GetMapping("/{id}")
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = Task::class)
    )
    suspend fun get(@PathVariable id: String): ResponseEntity<Task> {
        return taskService.findById(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = Task::class)
    )
    suspend fun create(@RequestBody createTaskRequest: CreateTaskRequest): ResponseEntity<Task> {
        return taskService.create(createTaskRequest).let {
            ResponseEntity.ok(it)
        }
    }

    @PostMapping("/{id}")
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = Task::class)
    )
    suspend fun update(@PathVariable id: String, @RequestBody updateTaskRequest: UpdateTaskRequest): ResponseEntity<Task> {
        return taskService.update(id, updateTaskRequest)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = Task::class)
    )
    suspend fun delete(@PathVariable id: String): ResponseEntity<Task> {
        return taskService.delete(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }
}