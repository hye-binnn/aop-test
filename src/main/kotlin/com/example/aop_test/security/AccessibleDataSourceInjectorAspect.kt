package com.example.aop_test.security

import com.example.aop_test.context.AccessibleDataSourceContext
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.nio.file.AccessDeniedException

@Aspect
@Component
class AccessibleDataSourceInjectorAspect(
    private val resolver: AccessibleDataSourceIdProvider,
    private val context: AccessibleDataSourceContext
) {

    @Before("@annotation(ResolveAccessibleDataSourceIds)")
    fun injectAccessibleIds() {
        val username = SecurityContextHolder.getContext().authentication?.name
            ?: throw AccessDeniedException("Unauthenticated")


        val accessibleIds = resolver.resolveAccessibleDataSourceIds(username)
        context.accessibleDataSourceIds = accessibleIds
    }
}
