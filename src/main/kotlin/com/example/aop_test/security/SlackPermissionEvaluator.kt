package com.example.aop_test.security

import com.example.aop_test.domain.DataSourceType
import com.example.aop_test.service.InMemoryStorage
import org.springframework.stereotype.Component

@Component
class SlackPermissionEvaluator : DataSourcePermissionEvaluator{
    override fun supports(dataSourceType: DataSourceType): Boolean {
        return dataSourceType == DataSourceType.SLACK
    }

    // 해당 데이터소스에 접근 권한이 있는가?
    // 아래 코드: 특정 프로젝트에 대한 접근 권한을 검사. 프로젝트와 엮인 데이터소스에 접근 권한이 없다면, false(접근 불가)를 반환.)
    override fun hasAccess(username: String, resourceId: Long): Boolean {
        // 실제 권한 검사 로직을 구현합니다.
        val project = InMemoryStorage.projects.firstOrNull { it.id == resourceId }
        val datasource = project?.dataSource ?: return false

        // 슬랙 캐시 서비스에서 저장한 캐시로부터 채널 내 멤버 리스트 가져오기
        // val members = slackMemberCache.getMembers(datasource.id)



        return !datasource.isPrivate || username in datasource.members
    }
}