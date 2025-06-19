package com.example.aop_test.service

import org.springframework.stereotype.Service

@Service
class DataSourcePrivacyService {

    fun hasAccessToDataSource(dataSourceId: Long, username: String): Boolean {
        val dataSource = InMemoryStorage.dataSources.firstOrNull {it.id == dataSourceId} ?: return false

        // If not private, everyone has access
        if (!dataSource.isPrivate) {
            return true
        }

        // Check if this user has explicit access
        return InMemoryStorage.accessibility
            .any { it.dataSource.id == dataSourceId && it.username == username }
    }

    fun getAccessibleDataSourceIds(username: String): List<Long> {
        val publicDataSourceIds = InMemoryStorage.dataSources
            .filter { !it.isPrivate }
            .map { it.id }

        val privateDataSourceIds = InMemoryStorage.accessibility
            .filter { it.username == username }
            .map { it.dataSource.id }

        return (publicDataSourceIds + privateDataSourceIds).distinct()
    }
}