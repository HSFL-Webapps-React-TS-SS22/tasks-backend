package de.progeek.tasks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TasksBackendApplication

fun main(args: Array<String>) {
    runApplication<TasksBackendApplication>(*args)
}
