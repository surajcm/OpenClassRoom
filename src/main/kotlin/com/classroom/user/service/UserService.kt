package com.classroom.user.service

import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException

interface UserService {

    @Throws(UserException::class)
    fun logIn(user: UserVO?): UserVO?

    @Throws(UserException::class)
    fun getAllUserDetails(): List<UserVO?>?

    @Throws(UserException::class)
    fun addNewUser(user: UserVO?)

    @Throws(UserException::class)
    fun getUserDetailsFromID(id: Long?): UserVO?

    @Throws(UserException::class)
    fun updateUser(user: UserVO?)

    @Throws(UserException::class)
    fun deleteUser(id: Long?)

    @Throws(UserException::class)
    fun searchUserDetails(searchUser: UserVO?): List<UserVO?>?

    fun save(user: UserVO?): UserVO?
}