package com.classroom.user.service.impl

import com.classroom.user.dao.UserDAO
import com.classroom.user.dao.impl.entities.Role
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.service.UserService
import jakarta.transaction.Transactional
import org.apache.commons.logging.LogFactory
import org.springframework.data.domain.Page
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class UserServiceImpl(
    private val userDAO: UserDAO,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    private val log = LogFactory.getLog(javaClass)

    override fun getAllUserDetails(pageNumber: Int): Page<User> =
        userDAO.getAllUserDetails(pageNumber)

    override fun addNewUser(user: User) {
        userDAO.addNewUser(user)
    }

    override fun searchUserDetails(searchUser: User, startsWith: Boolean, includes: Boolean): List<User> =
        userDAO.searchUserDetails(searchUser, startsWith, includes)

    override fun save(user: User): User {
        user.password = when {
            user.id != null -> {
                val existingUser = userDAO.findById(user.id!!)
                when {
                    user.password.isNotBlank() -> passwordEncoder.encode(user.password)
                    else -> existingUser?.password ?: ""
                }
            }
            else -> passwordEncoder.encode(user.password)
        }
        return userDAO.save(user)
    }

    override fun listRoles(): List<Role> = userDAO.listRoles()

    override fun isEmailUnique(id: Long?, email: String): Boolean {
        val userByEmail = userDAO.findByEmail(email) ?: return true
        return when (id) {
            null -> false
            else -> userByEmail.id == id
        }
    }

    override fun getUserById(id: Long): User? = userDAO.findById(id)

    override fun delete(id: Long) = userDAO.delete(id)

    override fun updateUserEnabledStatus(id: Long, status: Boolean) =
        userDAO.updateUserEnabledStatus(id, status)
}