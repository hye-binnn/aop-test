package com.example.aop_test.security

import kotlin.reflect.KClass

interface DataSourcePermissionEvaluator {
    fun supports(clazz: KClass<*>): Boolean
    fun hasAccess(username: String, resourceId: Long): Boolean
    
    // filtered list of accessible resource IDs (task, project, etc.)
    fun filterAccessibleIds(
        username: String,
        resourceIds: List<Long>
    ): List<Long>
}