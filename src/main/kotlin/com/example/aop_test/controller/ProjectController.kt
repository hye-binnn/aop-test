package com.example.aop_test.controller

import com.example.aop_test.domain.Project
import com.example.aop_test.domain.Task
import com.example.aop_test.security.CheckDataAccess
import com.example.aop_test.service.ProjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProjectController(private val projectService: ProjectService) {

    @GetMapping("/projects/{id}")
    @CheckDataAccess(resourceType = Project::class, resourceIdParam = "id")
    fun getProject(@PathVariable id: Long): Project? {
        return projectService.getProject(id)
    }

    @GetMapping("/tasks")
    @CheckDataAccess(resourceType = Task::class, resourceIdsParam = "ids")
    fun getAllTasks(@RequestParam ids: List<Long> = emptyList()): List<Task> {
        return projectService.getAllTasks(ids)
    }
}
