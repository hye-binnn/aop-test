package com.example.aop_test.security

import com.example.aop_test.domain.DataSourceType
import org.springframework.stereotype.Component

@Component
class PermissionEvaluatorRegistry(
    private val evaluators: List<DataSourcePermissionEvaluator> // Spring이 모든 evaluator 주입
) {

    // 데이터소스 타입에 맞는 평가기를 찾는 메서드
    fun getEvaluatorForDataSource(dataSourceType: DataSourceType): DataSourcePermissionEvaluator {
        return evaluators.firstOrNull { it.supports(dataSourceType) }
            ?: throw IllegalArgumentException("No evaluator found for data source type: $dataSourceType")
    }
}