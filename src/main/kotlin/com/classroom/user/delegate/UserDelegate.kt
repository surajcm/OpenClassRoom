package com.classroom.user.delegate

import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException
import com.classroom.user.service.UserService

class UserDelegate(private val userService: UserService) {

    @Throws(UserException::class)
    fun logIn(user: UserVO?): UserVO? {
        return userService.logIn(user)
    }

    @Throws(UserException::class)
    fun getAllUserDetails(): List<UserVO?>? {
        return userService.getAllUserDetails()
    }

    @Throws(UserException::class)
    fun addNewUser(user: UserVO?) {
        userService.addNewUser(user)
    }

    @Throws(UserException::class)
    fun getUserDetailsFromID(id: Long?): UserVO? {
        return userService.getUserDetailsFromID(id)
    }

    @Throws(UserException::class)
    fun updateUser(user: UserVO?) {
        userService.updateUser(user)
    }

    @Throws(UserException::class)
    fun deleteUser(id: Long?) {
        userService.deleteUser(id)
    }

    @Throws(UserException::class)
    fun searchUser(searchUser: UserVO?): List<UserVO?>? {
        return userService.searchUserDetails(searchUser)
    }
}