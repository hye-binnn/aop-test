package com.example.aop_test.service

import com.example.aop_test.context.AccessibleDataSourceContext
import com.example.aop_test.domain.DataSource
import com.example.aop_test.domain.DataSourceType
import com.example.aop_test.domain.Project
import com.example.aop_test.domain.Task
import org.springframework.stereotype.Service

@Service
class ProjectService(
    private val context: AccessibleDataSourceContext
) {
    fun getProject(id: Long): Project? = InMemoryStorage.projects.first { it.id == id }

    fun getAllTasks(): List<Task> {
        val accessibleDataSourceIds = context.accessibleDataSourceIds

        // 실제로는 ids 가지고 repo filtering 된 페이지 데이터 반환
        val filteredTasks = InMemoryStorage.tasks
            .filter { it.project.dataSource.id in accessibleDataSourceIds }

        return filteredTasks}
}


object InMemoryStorage {
    val dataSources = listOf(
        DataSource(
            id = 1L,
            type = DataSourceType.SLACK,
            content = "Public DataSource",
            isPrivate = false,
            members = emptyList()
        ),
        DataSource(
            id = 2L,
            type = DataSourceType.SLACK,
            content = "Private DS (user1 only)",
            isPrivate = true,
            members = listOf("user1")
        ),
        // user1일 때는 접근 불가능
        DataSource(
            id = 3L,
            type = DataSourceType.SLACK,
            content = "Private DS (user2, user3)",
            isPrivate = true,
            members = listOf("user2", "user3")
        )
    )

    val projects = listOf(
        Project(
            id = 1L,
            name = "Public Project",
            dataSource = dataSources[0]
        ),
        Project(
            id = 2L,
            name = "Private Project 1",
            dataSource = dataSources[1]
        ),
        Project(
            id = 3L,
            name = "Private Project 2",
            dataSource = dataSources[2]
        )
    )

    val tasks = listOf(
        Task(
            id = 1L,
            name = "Task A",
            project = projects[0]
        ),
        Task(
            id = 2L,
            name = "Task B",
            project = projects[1]
        ),
        Task(
            id = 3L,
            name = "Task C",
            project = projects[2]
        ),
        Task(
            id = 4L,
            name = "Task 4",
            project = projects[1]
        ),
        Task(
            id = 5L,
            name = "Task 5",
            project = projects[2]
        ),
        Task(
            id = 6L,
            name = "Task 6",
            project = projects[2]
        )
    )
}
