package com.classroom.user.dao.impl

import com.classroom.user.dao.impl.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>,
    JpaSpecificationExecutor<User> {
    fun findByEmail(email: String?): User?
}