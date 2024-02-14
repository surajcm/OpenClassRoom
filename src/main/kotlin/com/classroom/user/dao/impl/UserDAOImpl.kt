package com.classroom.user.dao.impl

import com.classroom.init.specs.SearchCriteria
import com.classroom.init.specs.SearchOperation
import com.classroom.user.dao.UserDAO
import com.classroom.user.dao.impl.entities.Role
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.dao.spec.UserSpecification
import com.classroom.user.exception.UserException
import com.classroom.user.exception.UserExceptionType.DATABASE_ERROR
import com.classroom.user.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils

@Repository
@SuppressWarnings("unused")
open class UserDAOImpl(private val userRepository: UserRepository, private val roleRepository: RoleRepository): UserDAO {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(UserException::class)
    override fun getAllUserDetails(): List<User> {
        val userList: List<User> = try {
            userRepository.findAll()
        } catch (ex: DataAccessException) {
            throw UserException(DATABASE_ERROR)
        }
        return userList
    }

    @Throws(UserException::class)
    override fun addNewUser(user: User) {
        try {
            userRepository.save(user)
        } catch (ex: DataAccessException) {
            ex.printStackTrace()
            throw UserException(DATABASE_ERROR)
        }
    }

    /**
     * search for all the user details from the database.
     *
     * @param searchUser UserVO
     * @return List of users
     * @throws DataAccessException on error
     */
    @Throws(DataAccessException::class)
    fun searchAllUsers(searchUser: User, startsWith: Boolean , includes: Boolean): List<User> {
        val userSpec = UserSpecification()
        val search: SearchOperation = populateSearchOperation(startsWith, includes)
        if (StringUtils.hasText(searchUser.firstName)) {
            userSpec.add(SearchCriteria("first_name", searchUser.firstName, search))
        }
        if (StringUtils.hasText(searchUser.email)) {
            userSpec.add(SearchCriteria("email", searchUser.email, search))
        }
        if (StringUtils.hasText(searchUser.roles.toString())) {
            userSpec.add(SearchCriteria("roles", searchUser.roles.toString(), search))
        }
        return userRepository.findAll(userSpec)
    }

    private fun populateSearchOperation(
        startsWith: Boolean,
        includes: Boolean
    ): SearchOperation {
        val searchOperation = if (includes) {
            SearchOperation.MATCH
        } else if (startsWith) {
            SearchOperation.MATCH_START
        } else {
            SearchOperation.EQUAL
        }
        return searchOperation
    }


    /**
     * updateUser.
     *
     * @param userVO user
     */
    override fun updateUser(user: User) {
        if (user.id != null) {
            val updatedUser :User =  userRepository.getReferenceById(user.id!!)
            updatedUser.firstName = user.firstName
            updatedUser.lastName = user.lastName
            updatedUser.email = user.email
            updatedUser.password = user.password
            updatedUser.roles = user.roles
            updatedUser.enabled = user.enabled
            updatedUser.modifiedBy = user.modifiedBy
        }
    }


    /**
     * delete user.
     *
     * @param id id
     */
    override fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

    /**
     * search for a list of users.
     *
     * @param searchUser UserVO
     * @return List of User
     * @throws UserException on error
     */
    @Throws(UserException::class)
    override fun searchUserDetails(searchUser: User, startsWith: Boolean , includes: Boolean): List<User>? {
        val userList: List<User> = try {
            searchAllUsers(searchUser, startsWith, includes)
        } catch (ex: DataAccessException) {
            throw UserException(DATABASE_ERROR)
        }
        return userList
    }

    override fun findByEmail(email: String): User? {
        logger.info(" at findByUsername")
        val user = userRepository.findByEmail(email)
        logger.info("user is {}", user)
        return user
    }

    override fun save(user: User?): User? {
        logger.info(" at save")
        return user?.let { userRepository.save(it) }
    }

    override fun listRoles(): List<Role> {
        return roleRepository.findAll().toList()
    }

    override fun findById(id: Long): User? {
        try {
            return userRepository.findById(id).get()
        } catch (ex: NoSuchElementException) {
            throw UserNotFoundException("User not found with id $id")
        }
    }

}