package com.classroom.user.dao

import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException

interface UserDAO {
    /**
     * log in dao.
     *
     * @param user user
     * @return User instance from database
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun logIn(user: UserVO?): UserVO?

    /**
     * getAllUserDetails to list all user details.
     *
     * @return List of User
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun getAllUserDetails(): List<UserVO?>?

    /**
     * create new user.
     *
     * @param user user
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun addNewUser(user: UserVO?)

    /**
     * getUserDetailsFromID to get the single user details from its id.
     *
     * @param id id
     * @return UserVO
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun getUserDetailsFromID(id: Long?): UserVO?

    /**
     * updates the current user details.
     *
     * @param user user
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun updateUser(user: UserVO?)

    /**
     * deletes the selected user.
     *
     * @param id id of the user
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun deleteUser(id: Long?)

    /**
     * search for a list of users.
     *
     * @param searchUser UserVO
     * @return List of User
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun searchUserDetails(searchUser: UserVO?): List<UserVO?>?

    fun findByEmail(email: String?): UserVO?
}