package com.classroom.user.dao.spec

import com.classroom.init.specs.SearchCriteria
import com.classroom.init.specs.SearchOperation
import com.classroom.user.dao.impl.entities.User
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.util.*

class UserSpecification : Specification<User> {
    companion object {
        private const val serialVersionUID: Long = 4328743
    }

    private var list: MutableList<SearchCriteria> = ArrayList()

    fun add(criteria: SearchCriteria) {
        list.add(criteria)
    }

    override fun toPredicate(root: Root<User>, query: CriteriaQuery<*>?, builder: CriteriaBuilder): Predicate? {
        val predicates: MutableList<Predicate> = ArrayList()

        for (criteria in list) {
            when (criteria.operation) {
                SearchOperation.EQUAL -> {
                    predicates.add(builder.equal(root.get<Any>(criteria.key), criteria.value))
                }
                SearchOperation.MATCH -> {
                    predicates.add(
                        builder.like(
                            builder.lower(root.get(criteria.key)),
                            "%" + criteria.value.toString().lowercase(Locale.getDefault()) + "%"
                        )
                    )
                }
                SearchOperation.MATCH_START -> {
                    predicates.add(
                        builder.like(
                            builder.lower(root.get(criteria.key)),
                            criteria.value.toString().lowercase(Locale.getDefault()) + "%"
                        )
                    )
                }
                else -> {
                    // Handle other operations if needed
                }
            }
        }
        return builder.and(*predicates.toTypedArray())
    }
}