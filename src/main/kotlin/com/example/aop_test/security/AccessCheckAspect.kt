package com.example.aop_test.security

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Aspect
@Component
class AccessCheckAspect(
    private val evaluatorRegistry: PermissionEvaluatorRegistry
) {
    @Around("@annotation(checkDataAccess)")
    fun checkAccess(joinPoint: ProceedingJoinPoint, checkDataAccess: CheckDataAccess): Any? {
        println("Aspect 호출 완료")
        // public이면 리턴 ? or public 캐싱처리 (in controller)


        // 리소스 타입과 ID 파라미터 이름을 가져옴
        val dataSourceType = checkDataAccess.dataSourceType
        val idParamName = checkDataAccess.idParam

        // 메서드 인자에서 ID 값을 찾음
        // 만약 컨트롤러 파라미터로 dataSourceType도 받아올 예정이라면 값을 찾아야 할 수도.
        // 현재는 Aspect 인자로 리소스 타입을 지원하는 쪽으로 생각 중
        val methodSignature = joinPoint.signature as MethodSignature
        val parameterNames = methodSignature.parameterNames
        val args = joinPoint.args

        val idIndex = parameterNames.indexOf(idParamName)

        if (idIndex == -1) {
            throw IllegalArgumentException("ID parameter '$idParamName' not found in method: ${methodSignature.name}")
        }

        val idValue = args[idIndex] as Long

        // 해당 데이터소스에 대한 접근 평가기 등록
        val evaluator = evaluatorRegistry.getEvaluatorForDataSource(dataSourceType)
        val username = SecurityContextHolder.getContext().authentication.name

        println(username)
        // 접근 권한을 체크
        if (!evaluator.hasAccess(username, idValue)) {
            throw SecurityException("Access denied for dataSource type: $dataSourceType with ID: $idValue")
        }

        // 접근이 허용되면 원래 메서드를 실행
        return joinPoint.proceed()
    }
}