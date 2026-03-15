package com.classroom.user.dao.impl

import com.classroom.init.Constants
import com.classroom.init.specs.SearchCriteria
import com.classroom.init.specs.SearchOperation
import com.classroom.user.dao.UserDAO
import com.classroom.user.dao.impl.entities.Role
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.dao.spec.UserSpecification
import com.classroom.user.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserDAOImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) : UserDAO {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getAllUserDetails(pageNumber: Int): Page<User> =
        userRepository.findAll(PageRequest.of(pageNumber - 1, Constants.USERS_PER_PAGE))

    override fun addNewUser(user: User) {
        userRepository.save(user)
    }

    override fun updateUser(user: User) {
        user.id?.let { userId ->
            userRepository.findByIdOrNull(userId)?.apply {
                firstName = user.firstName
                lastName = user.lastName
                email = user.email
                password = user.password
                roles = user.roles
                enabled = user.enabled
                modifiedBy = user.modifiedBy
            }
        }
    }

    override fun searchUserDetails(searchUser: User, startsWith: Boolean, includes: Boolean): List<User> {
        val userSpec = UserSpecification()
        val searchOperation = when {
            includes -> SearchOperation.MATCH
            startsWith -> SearchOperation.MATCH_START
            else -> SearchOperation.EQUAL
        }

        searchUser.firstName.takeIf { it.isNotBlank() }?.let {
            userSpec.add(SearchCriteria("first_name", it, searchOperation))
        }
        searchUser.email.takeIf { it.isNotBlank() }?.let {
            userSpec.add(SearchCriteria("email", it, searchOperation))
        }
        searchUser.roles.takeIf { it.isNotEmpty() }?.toString()?.let {
            userSpec.add(SearchCriteria("roles", it, searchOperation))
        }

        return userRepository.findAll(userSpec)
    }

    override fun findByEmail(email: String): User? {
        logger.info("Finding user by email")
        return userRepository.findByEmail(email).also {
            logger.info("User found: {}", it)
        }
    }

    override fun save(user: User): User {
        logger.info("Saving user")
        return userRepository.save(user)
    }

    override fun listRoles(): List<Role> = roleRepository.findAll()

    override fun findById(id: Long): User? =
        userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("User not found with id $id")

    override fun delete(id: Long) {
        userRepository.deleteById(id)
    }

    override fun updateUserEnabledStatus(id: Long, status: Boolean) {
        userRepository.updateEnabledStatus(id, status)
    }
}