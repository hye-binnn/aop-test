package com.example.aop_test.domain

data class DataSource(
    val id: Long,
    val type: DataSourceType,
    val content: String,
    val isPrivate: Boolean,
    val members: List<String>
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

enum class DataSourceType {
    SLACK,
    JIRA
}