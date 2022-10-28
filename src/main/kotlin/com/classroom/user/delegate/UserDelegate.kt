package com.classroom.user.delegate

import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException
import com.classroom.user.service.UserService

class UserDelegate {
    /**
     * user service instance.
     */
    private var userService: UserService? = null

    /**
     * spring setter for user service.
     *
     * @param userService userService instance
     */
    @Suppress("unused")
    fun setUserService(userService: UserService?) {
        this.userService = userService
    }

    /**
     * action for log in.
     *
     * @param user user instance
     * @return User instance from database
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun logIn(user: UserVO?): UserVO? {
        return userService!!.logIn(user)
    }

    /**
     * getAllUserDetails to list all user details.
     *
     * @return List of User
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun getAllUserDetails(): List<UserVO?>? {
        return userService!!.allUserDetails
    }

    /**
     * create new user.
     *
     * @param user user
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun addNewUser(user: UserVO?) {
        userService!!.addNewUser(user)
    }

    /**
     * getUserDetailsFromID to get the single user details from its id.
     *
     * @param id id
     * @return UserVO
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun getUserDetailsFromID(id: Long?): UserVO? {
        return userService!!.getUserDetailsFromID(id)
    }

    /**
     * updates the current user.
     *
     * @param user user
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun updateUser(user: UserVO?) {
        userService!!.updateUser(user)
    }

    /**
     * deletes the selected user.
     *
     * @param id id of the user
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun deleteUser(id: Long?) {
        userService!!.deleteUser(id)
    }

    /**
     * search for a list of users.
     *
     * @param searchUser UserVO
     * @return List of User
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun searchUser(searchUser: UserVO?): List<UserVO?>? {
        return userService!!.searchUserDetails(searchUser)
    }
}