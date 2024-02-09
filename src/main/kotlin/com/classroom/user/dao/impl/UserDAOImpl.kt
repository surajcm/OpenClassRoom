package com.classroom.user.dao.impl

import com.classroom.init.specs.SearchCriteria
import com.classroom.init.specs.SearchOperation
import com.classroom.user.dao.UserDAO
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.dao.spec.UserSpecification
import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException
import com.classroom.user.exception.UserExceptionType.DATABASE_ERROR
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import java.util.function.Consumer


@Repository
@SuppressWarnings("unused")
open class UserDAOImpl(private val userRepository: UserRepository): UserDAO {
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
    override fun updateUser(userVO: UserVO) {
        if (userVO.id != null) {
            val user :User =  userRepository.getReferenceById(userVO.id!!)
            user.firstName = userVO.firstName
            user.lastName = userVO.lastName
            user.email = userVO.email
            user.password = userVO.password
            user.roles = userVO.roles
            user.enabled = userVO.enabled
            user.modifiedBy = userVO.lastModifiedBy
        }
    }

    private fun convertToUser(userVO: UserVO): User {
        val user = User()
        user.firstName = userVO.firstName
        user.lastName = userVO.lastName
        user.email = userVO.email
        user.password = userVO.password
        user.roles = userVO.roles
        user.enabled = userVO.enabled
        user.createdBy = userVO.createdBy
        user.modifiedBy = userVO.lastModifiedBy
        return user
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
        return user?.let { userRepository.save(it) }
    }


}