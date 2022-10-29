package com.classroom.user.service.impl

import com.classroom.user.dao.UserDAO
import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException
import com.classroom.user.service.UserService
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userDAO: UserDAO): UserService {
    private val MESSAGE = "Exception type in service impl: {} "

    private val log = LogFactory.getLog(javaClass)


    @Throws(UserException::class)
    override fun logIn(user: UserVO?): UserVO? {
        var realUser: UserVO? = null
        try {
            log.info(" Inside user service impl")
            realUser = userDAO.logIn(user!!)
        } catch (ex: UserException) {
            log.error("Exception type in service impl : {}", ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return realUser
    }

    @Throws(UserException::class)
    override fun getAllUserDetails(): List<UserVO?>? {
        var userList: List<UserVO?>? = null
        try {
            userList = userDAO.getAllUserDetails()
        } catch (ex: UserException) {
            log.error(MESSAGE, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return userList
    }

    @Throws(UserException::class)
    override fun addNewUser(user: UserVO?) {
        try {
            userDAO.addNewUser(user!!)
        } catch (ex: UserException) {
            log.error(MESSAGE, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    @Throws(UserException::class)
    override fun getUserDetailsFromID(id: Long?): UserVO? {
        var userVO: UserVO? = null
        try {
            userVO = userDAO.getUserDetailsFromID(id!!)
        } catch (ex: UserException) {
            log.error(MESSAGE, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return userVO
    }

    @Throws(UserException::class)
    override fun updateUser(user: UserVO?) {
        try {
            userDAO.updateUser(user!!)
        } catch (ex: UserException) {
            log.error(MESSAGE, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    @Throws(UserException::class)
    override fun deleteUser(id: Long?) {
        try {
            userDAO.deleteUser(id!!)
        } catch (ex: UserException) {
            log.error(MESSAGE, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    @Throws(UserException::class)
    override fun searchUserDetails(searchUser: UserVO?): List<UserVO?>? {
        var userList: List<UserVO?>? = null
        try {
            userList = userDAO.searchUserDetails(searchUser!!)
        } catch (ex: UserException) {
            log.error(MESSAGE, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return userList
    }
}