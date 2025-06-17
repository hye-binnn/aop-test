package com.example.aop_test.security

import com.example.aop_test.domain.DataSourceType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckDataAccess(
    val dataSourceType: DataSourceType,
    val idParam: String
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ResolveAccessibleDataSourceIds