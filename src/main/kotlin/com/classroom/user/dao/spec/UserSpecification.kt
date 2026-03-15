package com.classroom.user.dao.spec

import com.classroom.init.specs.SearchCriteria
import com.classroom.init.specs.SearchOperation
import com.classroom.user.dao.impl.entities.User
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class UserSpecification : Specification<User> {
    companion object {
        private const val serialVersionUID: Long = 4328743
    }

    private val criteria = mutableListOf<SearchCriteria>()

    fun add(searchCriteria: SearchCriteria) {
        criteria.add(searchCriteria)
    }

    override fun toPredicate(
        root: Root<User>,
        query: CriteriaQuery<*>?,
        builder: CriteriaBuilder
    ): Predicate {
        val predicates = criteria.map { criterion ->
            when (criterion.operation) {
                SearchOperation.EQUAL ->
                    builder.equal(root.get<Any>(criterion.key), criterion.value)

                SearchOperation.MATCH ->
                    builder.like(
                        builder.lower(root.get(criterion.key)),
                        "%${criterion.value.toString().lowercase()}%"
                    )

                SearchOperation.MATCH_START ->
                    builder.like(
                        builder.lower(root.get(criterion.key)),
                        "${criterion.value.toString().lowercase()}%"
                    )
            }
        }
        return builder.and(*predicates.toTypedArray())
    }
}