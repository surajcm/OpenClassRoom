package com.classroom.user.dao

import com.classroom.user.dao.impl.entities.User
import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException

interface UserDAO {

    @Throws(UserException::class)
    fun getAllUserDetails(): List<User>

    @Throws(UserException::class)
    fun addNewUser(user: User)

    @Throws(UserException::class)
    fun updateUser(userVO: UserVO)

    @Throws(UserException::class)
    fun deleteUser(id: Long)

    @Throws(UserException::class)
    fun searchUserDetails(searchUser: User, startsWith: Boolean , includes: Boolean): List<User?>?

    fun findByEmail(email: String): User?

    fun save(user: User?): User?
}