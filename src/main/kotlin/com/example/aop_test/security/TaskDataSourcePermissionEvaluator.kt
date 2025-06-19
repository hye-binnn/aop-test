package com.example.aop_test.security

import com.example.aop_test.service.DataSourcePrivacyService
import com.example.aop_test.domain.Task
import com.example.aop_test.service.InMemoryStorage
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class TaskDataSourcePermissionEvaluator(
    private val dataSourcePrivacyService: DataSourcePrivacyService
) : DataSourcePermissionEvaluator {
    override fun supports(clazz: KClass<*>): Boolean {
        return clazz == Task::class
    }

    override fun hasAccess(username: String, resourceId: Long): Boolean {
        val task = InMemoryStorage.tasks.firstOrNull { it.id == resourceId } ?: throw IllegalArgumentException("Task with ID $resourceId not found")
        val dataSourceId = task.project.dataSource.id

        return dataSourcePrivacyService.hasAccessToDataSource(dataSourceId, username)
    }

    override fun filterAccessibleIds(username: String, resourceIds: List<Long>): List<Long> {
        // Accessible data source IDs for the user
        val accessibleDataSourceIds = dataSourcePrivacyService.getAccessibleDataSourceIds(username)

        // Filter task IDs by accessible data source IDs
        return InMemoryStorage.tasks
            .filter { task -> task.project.dataSource.id in accessibleDataSourceIds }
            .map { it.id }
            .distinct()
    }
}