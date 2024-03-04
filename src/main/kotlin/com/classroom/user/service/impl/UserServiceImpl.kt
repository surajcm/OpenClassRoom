package com.classroom.user.service.impl

import com.classroom.user.dao.UserDAO
import com.classroom.user.dao.impl.entities.Role
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.exception.UserException
import com.classroom.user.service.UserService
import jakarta.transaction.Transactional
import org.apache.commons.logging.LogFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
@Transactional
open class UserServiceImpl(private val userDAO: UserDAO): UserService {
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

    override fun save(user: User): User {
        if (user.id != null) {
            val existingUser = userDAO.findById(user.id!!)
            if (user.password?.isEmpty() != true) {
                user.password = bcryptPasswordEncoder.encode(user.password)
            } else {
                user.password = existingUser?.password
            }
        } else {
            user.password = bcryptPasswordEncoder.encode(user.password)
        }
        return userDAO.save(user)
    }

    override fun listRoles(): List<Role> {
        return userDAO.listRoles()
    }

    override fun isEmailUnique(id: Long?, email: String): Boolean {
        val userByEmail = userDAO.findByEmail(email) ?: return true
        return if (isCreatingNew(id)) userByEmail == null else userByEmail.id == id
    }

    private fun isCreatingNew(id: Long?): Boolean {
        return id == null
    }


    override fun getUserById(id: Long): User? {
        return userDAO.findById(id)
    }

    override fun delete(id: Long) {
        userDAO.delete(id)
    }

    override fun updateUserEnabledStatus(id: Long, status: Boolean) {
        userDAO.updateUserEnabledStatus(id, status)
    }

}