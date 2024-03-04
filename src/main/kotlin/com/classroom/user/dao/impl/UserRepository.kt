package com.classroom.user.dao.impl

import com.classroom.user.dao.impl.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>,
    JpaSpecificationExecutor<User> {
    fun findByEmail(email: String): User?
    fun countById(id: Long): Long

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    fun updateEnabledStatus(id: Long, enabled: Boolean)
}