package com.example.aop_test.security

import com.example.aop_test.service.InMemoryStorage
import org.springframework.stereotype.Component

@Component
class InMemoryAccessibleDataSourceIdProvider : AccessibleDataSourceIdProvider {
    override fun resolveAccessibleDataSourceIds(username: String): List<Long> {

        // 접근 가능한 데이터소스 ID 목록을 반환합니다. IN절을 사용하는 것이 성능과 안정성 굳
        // NOT IN은 인덱스를 잘 못 타고, Null 값 포함 시 오류 또는 결과 왜곡 발생
        return InMemoryStorage.dataSources
            .filter { !it.isPrivate || username in it.members }
            .map { it.id }
    }
}