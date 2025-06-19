package com.example.aop_test.security

import com.example.aop_test.service.DataSourcePrivacyService
import com.example.aop_test.domain.Project
import com.example.aop_test.service.InMemoryStorage
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class ProjectDataSourcePermissionEvaluator (
    private val dataSourcePrivacyService: DataSourcePrivacyService
) : DataSourcePermissionEvaluator {
    override fun supports(clazz: KClass<*>): Boolean {
        return clazz == Project::class
    }

    override fun hasAccess(username: String, resourceId: Long): Boolean {
        val project = InMemoryStorage.projects.firstOrNull { it.id == resourceId } ?: throw IllegalArgumentException("Project with ID $resourceId not found")
        val dataSourceId = project.dataSource.id

        return dataSourcePrivacyService.hasAccessToDataSource(dataSourceId, username)
    }

    override fun filterAccessibleIds(username: String, resourceIds: List<Long>): List<Long> {
        if (resourceIds.isEmpty()) {
            return emptyList()
        }

        // Accessible data source IDs for the user
        val accessibleDataSourceIds = dataSourcePrivacyService.getAccessibleDataSourceIds(username)

        return InMemoryStorage.projects
            .filter { project -> project.dataSource.id in accessibleDataSourceIds }
            .map { it.id }
            .distinct()
    }
}