package com.example.aop_test.security

interface AccessibleDataSourceIdProvider {
    fun resolveAccessibleDataSourceIds(username: String): List<Long>
}