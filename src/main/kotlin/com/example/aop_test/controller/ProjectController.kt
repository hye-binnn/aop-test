package com.example.aop_test.controller

import com.example.aop_test.domain.DataSourceType
import com.example.aop_test.domain.Project
import com.example.aop_test.domain.Task
import com.example.aop_test.security.ResolveAccessibleDataSourceIds
import com.example.aop_test.security.CheckDataAccess
import com.example.aop_test.service.ProjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProjectController(private val projectService: ProjectService) {

    @GetMapping("/projects/{id}")
    @CheckDataAccess(dataSourceType = DataSourceType.SLACK, idParam = "id")
    fun getProject(@PathVariable id: Long): Project? {
        return projectService.getProject(id)
    }

    @GetMapping("/tasks")
    @ResolveAccessibleDataSourceIds
    fun getAllTasks(): List<Task> {
        return projectService.getAllTasks()
    }
}
