package com.classroom.user.service.impl

import com.classroom.user.dao.UserDAO
import com.classroom.user.dao.impl.entities.Role
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.exception.UserException
import com.classroom.user.service.UserService
import org.apache.commons.logging.LogFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userDAO: UserDAO): UserService {
    private val message = "Exception type in service impl: {} "
    private val bcryptPasswordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    private val log = LogFactory.getLog(javaClass)

    @Throws(UserException::class)
    override fun getAllUserDetails(): List<User>? {
        var userList: List<User>? = null
        try {
            userList = userDAO.getAllUserDetails()
        } catch (ex: UserException) {
            log.error(message, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return userList
    }


    @Throws(UserException::class)
    override fun addNewUser(user: User?) {
        try {
            userDAO.addNewUser(user!!)
        } catch (ex: UserException) {
            log.error(message, ex.cause)
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
            log.error(message, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    @Throws(UserException::class)
    override fun searchUserDetails(searchUser: User?,startsWith: Boolean , includes: Boolean): List<User?>? {
        var userList: List<User?>? = null
        try {
            userList = userDAO.searchUserDetails(searchUser!!, startsWith, includes)
        } catch (ex: UserException) {
            log.error(message, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return userList
    }

    override fun save(user: User): User? {
        var userSaved : User? = null
        try {
            user.password = bcryptPasswordEncoder.encode(user.password)
            userSaved = userDAO.save(user)
        } catch (ex: UserException) {
            log.error(message, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return userSaved
    }

    override fun listRoles(): List<Role> {
        return userDAO.listRoles()
    }

    override fun isEmailUnique(email : String): Boolean {
        return userDAO.findByEmail(email) == null
    }

    override fun getUserById(id: Long): User? {
        return userDAO.findById(id)
    }

}