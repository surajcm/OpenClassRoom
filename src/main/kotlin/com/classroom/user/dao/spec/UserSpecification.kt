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
    val serialVersionUID: Long = 4328743

    private var list: MutableList<SearchCriteria>? = null

    fun UserSpecification() {
        this.list = ArrayList<SearchCriteria>()
    }

    fun add(criteria: SearchCriteria) {
        list!!.add(criteria)
    }
    override fun toPredicate(root: Root<User>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        //Create a new predicate list
        val predicates: MutableList<Predicate> = ArrayList()

        //Add criteria to predicates
        for (criteria in list!!) {
            if (criteria.getOperation() == SearchOperation.EQUAL) {
                /*predicates.add(
                    builder.equal(
                        root[criteria.getKey()], criteria.getValue()
                    )
                )*/
            } else if (criteria.getOperation() == SearchOperation.MATCH) {
                predicates.add(
                    builder.like(
                        builder.lower(root[criteria.getKey()]),
                        "%" + criteria.getValue().toString().lowercase(Locale.getDefault()) + "%"
                    )
                )
            } else if (criteria.getOperation() == SearchOperation.MATCH_START) {
                predicates.add(
                    builder.like(
                        builder.lower(root[criteria.getKey()]),
                        "%" + criteria.getValue().toString().lowercase(Locale.getDefault()) + "%"
                    )
                )
            }
        }

        return builder.and(*predicates.toTypedArray())
    }

}