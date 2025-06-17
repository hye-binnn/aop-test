package com.example.aop_test.context

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class AccessibleDataSourceContext {
    var accessibleDataSourceIds: List<Long> = emptyList()

//    private val ids = ThreadLocal<List<Long>>()
//
//    fun set(ids: List<Long>) { this.ids.set(ids) }
//    fun get(): List<Long> = ids.get() ?: emptyList()
//    fun clear() = ids.remove()
}
