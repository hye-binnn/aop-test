package com.example.aop_test.security

import com.example.aop_test.domain.DataSource
import com.example.aop_test.service.DataSourcePrivacyService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Aspect
@Component
class AccessCheckAspect(
    private val evaluators: List<DataSourcePermissionEvaluator>,
    private val dataSourcePrivacyService: DataSourcePrivacyService
) {
    @Around("@annotation(checkDataAccess)")
    fun checkAccess(joinPoint: ProceedingJoinPoint, checkDataAccess: CheckDataAccess): Any? {
        // 리소스 타입과 ID 파라미터 이름을 가져옴
        val resourceIdParamName = checkDataAccess.resourceIdParam
        val resourceIdsParamName = checkDataAccess.resourceIdsParam
        val userIdParamName = checkDataAccess.userIdParam
        val resourceType = checkDataAccess.resourceType

        val methodSignature = joinPoint.signature as MethodSignature
        val parameterNames = methodSignature.parameterNames
        val args = joinPoint.args

        val currentUserId = SecurityContextHolder.getContext().authentication.name

        evaluators.forEach { evaluator ->
            println("평가기: ${evaluator.javaClass.name}, 지원 여부: ${evaluator.supports(resourceType)}")
        }

        val evaluator = evaluators.firstOrNull { it.supports(resourceType) }
            ?: throw IllegalStateException("지원되지 않는 리소스 타입: ${resourceType.simpleName}")


        val idIndex = parameterNames.indexOf(resourceIdParamName)
        val idsIndex = parameterNames.indexOf(resourceIdsParamName)
        val userIdIndex = parameterNames.indexOf(userIdParamName)

        println("id index: $idIndex, ids index: $idsIndex, userId index: $userIdIndex")

        if (idIndex >= 0 && args[idIndex] is Long) {
            val indexId = args[idIndex] as Long

            val isAccessible = if (resourceType == DataSource::class) {
                dataSourcePrivacyService.hasAccessToDataSource(indexId, currentUserId)
            } else evaluator.hasAccess(currentUserId, indexId)

            if (!isAccessible) {
                throw AccessDeniedException("Access denied to this resource")
            }

            return joinPoint.proceed()
        }

        if (idsIndex >= 0 && args[idsIndex] is List<*>) {
            @Suppress("UNCHECKED_CAST")
            val indexIds = args[idsIndex] as List<Long>

            // 접근 가능한 ID 목록으로 필터링
            val accessibleIds = evaluator.filterAccessibleIds(currentUserId, indexIds)
            val newArgs = args.clone()
            newArgs[idsIndex] = accessibleIds

            // 업데이트된 인자로 원래 메서드를 실행
            return joinPoint.proceed(newArgs)
        }

        return joinPoint.proceed()
    }
}