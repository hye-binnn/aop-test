package com.example.aop_test.security

import com.example.aop_test.domain.DataSourceType

interface DataSourcePermissionEvaluator {
    fun supports(dataSourceType: DataSourceType): Boolean
    fun hasAccess(username: String, resourceId: Long): Boolean
}