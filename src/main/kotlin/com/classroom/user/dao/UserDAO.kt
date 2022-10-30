package com.classroom.user.dao

import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException

interface UserDAO {

    @Throws(UserException::class)
    fun logIn(userVO: UserVO): UserVO?

    @Throws(UserException::class)
    fun getAllUserDetails(): List<UserVO?>?

    @Throws(UserException::class)
    fun addNewUser(user: UserVO)

    @Throws(UserException::class)
    fun getUserDetailsFromID(id: Long): UserVO?

    @Throws(UserException::class)
    fun updateUser(userVO: UserVO)

    @Throws(UserException::class)
    fun deleteUser(id: Long)

    @Throws(UserException::class)
    fun searchUserDetails(searchUser: UserVO): List<UserVO?>?

    fun findByEmail(email: String): UserVO?

    fun save(user: UserVO?): UserVO?
}