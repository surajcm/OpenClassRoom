package com.classroom.user.dao.impl

import com.classroom.user.dao.impl.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: PagingAndSortingRepository<User, Long>, CrudRepository<User, Long>,
    JpaSpecificationExecutor<User> {

    fun findByEmail(email: String): User?

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    fun updateEnabledStatus(id: Long, enabled: Boolean)
}