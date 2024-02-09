package com.classroom.user.service.impl

import com.classroom.user.dao.UserDAO
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException
import com.classroom.user.service.UserService
import org.apache.commons.logging.LogFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.function.Consumer


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

    private fun convertUsersToUserVOs(users: List<User>): List<UserVO> {
        val userVOS: MutableList<UserVO> = ArrayList()
        users.forEach(Consumer { user: User ->
            val userVO = UserVO()
            userVO.id = user.id
            userVO.firstName = user.firstName
            userVO.email = user.email
            userVO.password = user.password
            userVO.roles = user.roles
            userVO.enabled = user.enabled
            userVO.createdBy = user.createdBy
            userVO.lastModifiedBy = user.modifiedBy
            userVOS.add(userVO)
        })
        return userVOS
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

    private fun convertToUser(userVO: UserVO?): User {
        val user = User()
        if (userVO != null) {
            user.firstName = userVO.firstName
            user.lastName = userVO.lastName
            user.email = userVO.email
            user.password = userVO.password
            user.roles = userVO.roles
            user.enabled = userVO.enabled
            user.createdBy = userVO.createdBy
            user.modifiedBy = userVO.lastModifiedBy
        }
        return user
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

    override fun save(user: User?): User? {
        var user : User? = null
        try {
            if (user != null) {
                user.password = bcryptPasswordEncoder.encode(user.password)
            }
            user = userDAO.save(user)
        } catch (ex: UserException) {
            log.error(message, ex.cause)
            throw UserException(ex.exceptionType!!)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return user
    }

}