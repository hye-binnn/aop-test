package com.example.aop_test.domain

data class DataSource(
    val id: Long,
    val type: DataSourceType,
    val content: String,
    val isPrivate: Boolean,
)

data class DataSourceAccessibility(
    val id: Long,
    val dataSource: DataSource,
    val username: String
)

data class Project(
    val id: Long,
    val name: String,
    val dataSource: DataSource
)

data class Task(
    val id: Long,
    val name: String,
    val project: Project,
)

data class Index(
    val id: Long,
    val name: String,
    val dataSource: DataSource
)

enum class DataSourceType {
    SLACK,
    GITHUB,
    JIRA,
}
