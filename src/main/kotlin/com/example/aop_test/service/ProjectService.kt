package com.example.aop_test.service

import com.example.aop_test.domain.*
import org.springframework.stereotype.Service

@Service
class ProjectService {
    fun getProject(id: Long): Project? = InMemoryStorage.projects.first { it.id == id }

    fun getAllTasks(ids: List<Long>): List<Task> {
        val filteredTasks = InMemoryStorage.tasks
            .filter { it.project.id in ids }

        return filteredTasks
    }
}


object InMemoryStorage {
    val dataSources = listOf(
        DataSource(
            id = 1L,
            type = DataSourceType.SLACK,
            content = "Public DataSource",
            isPrivate = false,
        ),
        DataSource(
            id = 2L,
            type = DataSourceType.SLACK,
            content = "Private DS (user1 only)",
            isPrivate = true,
        ),
        // user1일 때는 접근 불가능
        DataSource(
            id = 3L,
            type = DataSourceType.SLACK,
            content = "Private DS (user2, user3)",
            isPrivate = true,
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

    val index = listOf(
        Index(
            id = 1L,
            name = "Index Task A",
            dataSource = dataSources[0]
        ),
        Index(
            id = 2L,
            name = "Index Task B",
            dataSource = dataSources[1]
        ),
        Index(
            id = 3L,
            name = "Index Task C",
            dataSource = dataSources[2]
        ),
        Index(
            id = 4L,
            name = "Index Task D",
            dataSource = dataSources[2]
        )
    )

    val accessibility = listOf(
        DataSourceAccessibility(
            id = 2L,
            dataSource = dataSources[1],
            username = "user1"
        ),
        DataSourceAccessibility(
            id = 3L,
            dataSource = dataSources[2],
            username = "user2"
        ),
        DataSourceAccessibility(
            id = 4L,
            dataSource = dataSources[2],
            username = "user3"
        )
    )
}
