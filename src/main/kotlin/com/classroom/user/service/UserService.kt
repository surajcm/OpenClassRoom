package com.classroom.user.service

import com.classroom.user.dao.impl.entities.Role
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.exception.UserException

interface UserService {

    @Throws(UserException::class)
    fun getAllUserDetails(): List<User>?

    @Throws(UserException::class)
    fun addNewUser(user: User?)

    @Throws(UserException::class)
    fun searchUserDetails(searchUser: User?, startsWith: Boolean , includes: Boolean): List<User?>?

    fun save(user: User): User?

    fun listRoles(): List<Role>

    fun isEmailUnique(id:Long, email : String): Boolean

    fun getUserById(id: Long): User?

    fun delete(id: Long)
}