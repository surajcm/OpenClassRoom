package com.classroom.user.dao

import com.classroom.user.dao.impl.entities.Role
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.exception.UserException

interface UserDAO {

    @Throws(UserException::class)
    fun getAllUserDetails(): List<User>

    @Throws(UserException::class)
    fun addNewUser(user: User)

    @Throws(UserException::class)
    fun updateUser(user: User)

    @Throws(UserException::class)
    fun deleteUser(id: Long)

    @Throws(UserException::class)
    fun searchUserDetails(searchUser: User, startsWith: Boolean , includes: Boolean): List<User?>?

    fun findByEmail(email: String): User?

    fun save(user: User?): User?

    fun listRoles(): List<Role>

    fun findById(id: Long): User?
}