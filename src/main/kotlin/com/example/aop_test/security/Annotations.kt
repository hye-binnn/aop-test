package com.example.aop_test.security

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckDataAccess(
    val resourceType: KClass<*>,
    val resourceIdParam: String = "id",
    val resourceIdsParam: String = "ids",
    val userIdParam: String = "userId",
)
