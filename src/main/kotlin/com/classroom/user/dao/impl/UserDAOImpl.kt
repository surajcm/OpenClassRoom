package com.classroom.user.dao.impl

import com.classroom.init.specs.SearchCriteria
import com.classroom.init.specs.SearchOperation
import com.classroom.user.dao.UserDAO
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.dao.spec.UserSpecification
import com.classroom.user.domain.UserVO
import com.classroom.user.exception.UserException
import com.classroom.user.exception.UserExceptionType.DATABASE_ERROR
import com.classroom.user.exception.UserExceptionType.INCORRECT_PASSWORD
import com.classroom.user.exception.UserExceptionType.UNKNOWN_USER
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
    override fun logIn(userVO: UserVO): UserVO? {
        logger.info("At login, User vo is {}", userVO)
        val user: User? = try {
            userRepository.findByEmail(userVO.email)
        } catch (ex: DataAccessException) {
            logger.error(ex.localizedMessage)
            throw UserException(DATABASE_ERROR)
        }
        if (user != null) {
            logger.info(" user details fetched successfully,for user name {}", user.email)
            if (!user.password.equals(user.password, ignoreCase = true)) {
                throw UserException(INCORRECT_PASSWORD)
            }
            logger.info(" Password matched successfully, user details are {}", user)
        } else {
            // invalid user
            throw UserException(UNKNOWN_USER)
        }
        return convertTOUserVO(user)
    }

    /**
     * getAllUserDetails to list all user details.
     *
     * @return List of user
     * @throws UserException on db error
     */
    @Throws(UserException::class)
    override fun getAllUserDetails(): List<UserVO>? {
        val userList: List<UserVO> = try {
            val users = userRepository.findAll()
            convertUsersToUserVOs(users)
        } catch (ex: DataAccessException) {
            throw UserException(DATABASE_ERROR)
        }
        return userList
    }

    private fun convertUsersToUserVOs(users: List<User>): List<UserVO> {
        val userVOS: MutableList<UserVO> = ArrayList()
        users.forEach(Consumer { user: User ->
            val userVO = UserVO()
            userVO.id = user.userId
            userVO.firstName = user.firstName
            userVO.email = user.email
            userVO.password = user.password
            userVO.role = user.role
            userVO.createdBy = user.createdBy
            userVO.lastModifiedBy = user.modifiedBy
            userVOS.add(userVO)
        })
        return userVOS
    }

    /**
     * addNewUser to add a new user.
     *
     * @param user user
     * @throws UserException on error
     */
    @Throws(UserException::class)
    override fun addNewUser(user: UserVO) {
        try {
            save(user)
        } catch (ex: DataAccessException) {
            ex.printStackTrace()
            throw UserException(DATABASE_ERROR)
        }
    }

    /**
     * getUserDetailsFromID to get the single user details from its id.
     *
     * @param id id
     * @return UserVO
     * @throws UserException on error
     */
    @Throws(UserException::class)
    override fun getUserDetailsFromID(id: Long): UserVO? {
        logger.info("At getUserDetailsFromID")
        var userVO: UserVO? = null
        try {
            val optionalUser = userRepository.findById(id)
            if (optionalUser.isPresent) {
                val user = optionalUser.get()
                userVO = convertTOUserVO(user)
            }
        } catch (ex: DataAccessException) {
            logger.error(ex.localizedMessage)
            throw UserException(DATABASE_ERROR)
        }
        return userVO
    }

    /**
     * search for all the user details from the database.
     *
     * @param searchUser UserVO
     * @return List of users
     * @throws DataAccessException on error
     */
    @Throws(DataAccessException::class)
    fun searchAllUsers(searchUser: UserVO): List<UserVO> {

        val userSpec = UserSpecification()
        val search: SearchOperation = populateSearchOperation(searchUser.startsWith, searchUser.includes)
        if (StringUtils.hasText(searchUser.firstName)) {
            userSpec.add(SearchCriteria("first_name", searchUser.firstName, search))
        }
        if (StringUtils.hasText(searchUser.email)) {
            userSpec.add(SearchCriteria("email", searchUser.email, search))
        }
        if (StringUtils.hasText(searchUser.role)) {
            userSpec.add(SearchCriteria("role", searchUser.role, search))
        }
        val vos = userRepository.findAll(userSpec)
        return convertUsersToUserVOs(vos)
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
            user.role = userVO.role
            user.modifiedBy = userVO.lastModifiedBy
        }
    }


    /**
     * save to add a new user.
     *
     * @param userVO user
     * @throws UserException on error
     */
    @Throws(UserException::class)
    fun save(userVO: UserVO) {
        val user = convertToUser(userVO)
        try {
            userRepository.save(user)
        } catch (ex: DataAccessException) {
            logger.error(ex.localizedMessage)
            throw UserException(DATABASE_ERROR)
        }
    }

    private fun convertToUser(userVO: UserVO): User {
        val user = User()
        user.firstName = userVO.firstName
        user.lastName = userVO.lastName
        user.email = userVO.email
        user.password = userVO.password
        user.role = userVO.role
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
    override fun searchUserDetails(searchUser: UserVO): List<UserVO>? {
        val userList: List<UserVO> = try {
            searchAllUsers(searchUser)
        } catch (ex: DataAccessException) {
            throw UserException(DATABASE_ERROR)
        }
        return userList
    }

    override fun findByEmail(email: String): UserVO? {
        logger.info(" at findByUsername")
        val user = userRepository.findByEmail(email)
        logger.info("user is {}", user)
        return user?.let { convertTOUserVO(it) }
    }

    override fun save(user: UserVO?): UserVO? {
        val userEntity = user?.let { convertToUser(it) }
        val savedEntity = userEntity?.let { userRepository.save(it) }
        return savedEntity?.let { convertTOUserVO(it) }
    }

    private fun convertTOUserVO(user: User): UserVO? {
        val userVO = UserVO()
        userVO.id = user.userId
        userVO.firstName = user.firstName
        userVO.lastName = user.lastName
        userVO.email = user.email
        userVO.password = user.password
        userVO.role = user.role
        userVO.createdBy = user.createdBy
        userVO.lastModifiedBy = user.modifiedBy
        return userVO
    }
}