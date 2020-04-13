package com.classroom.user.dao.impl;

import com.classroom.user.dao.UserDAO;
import com.classroom.user.dao.entities.User;
import com.classroom.user.domain.UserVO;
import com.classroom.user.exception.UserException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings("unused")
public class UserDAOImpl implements UserDAO {

    private static final Logger LOG = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * LOG in.
     *
     * @param userVO userVO
     * @return user instance from database
     * @throws UserException on error
     */
    @Override
    public UserVO logIn(final UserVO userVO) throws UserException {
        User user;
        try {
            user = userRepository.findByLogInId(userVO.getLoginId());
        } catch (DataAccessException ex) {
            LOG.error(ex.getLocalizedMessage());
            throw new UserException(UserException.DATABASE_ERROR);
        }

        if (user != null) {
            LOG.info(" user details fetched successfully,for user name {}", user.getName());
            if (!userVO.getPassword().equalsIgnoreCase(user.getPassword())) {
                throw new UserException(UserException.INCORRECT_PASSWORD);
            }
            LOG.info(" Password matched successfully, user details are {}", user);
        } else {
            // invalid user
            throw new UserException(UserException.UNKNOWN_USER);
        }
        return convertTOUserVO(user);
    }

    /**
     * getAllUserDetails to list all user details.
     *
     * @return List of user
     * @throws UserException on db error
     */
    @Override
    public List<UserVO> getAllUserDetails() throws UserException {
        List<UserVO> userList;
        try {
            List<User> users = userRepository.findAll();
            userList = convertUsersToUserVOs(users);
        } catch (DataAccessException ex) {
            throw new UserException(UserException.DATABASE_ERROR);
        }
        return userList;
    }

    private List<UserVO> convertUsersToUserVOs(final List<User> users) {
        List<UserVO> userVOS = new ArrayList<>();
        users.forEach(user -> {
            UserVO userVO = new UserVO();
            userVO.setId(user.getUserId());
            userVO.setName(user.getName());
            userVO.setLoginId(user.getLogInId());
            userVO.setPassword(user.getPassword());
            userVO.setRole(user.getRole());
            userVO.setCreatedBy(user.getCreatedBy());
            userVO.setLastModifiedBy(user.getModifiedBy());
            userVOS.add(userVO);
        });
        return userVOS;
    }

    /**
     * addNewUser to add a new user.
     *
     * @param user user
     * @throws UserException on error
     */
    public void addNewUser(final UserVO user) throws UserException {
        try {
            save(user);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new UserException(UserException.DATABASE_ERROR);
        }
    }

    /**
     * getUserDetailsFromID to get the single user details from its id.
     *
     * @param id id
     * @return UserVO
     * @throws UserException on error
     */
    public UserVO getUserDetailsFromID(final Long id) throws UserException {
        UserVO userVO = null;
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                userVO = convertTOUserVO(user);
            }
        } catch (DataAccessException ex) {
            LOG.error(ex.getLocalizedMessage());
            throw new UserException(UserException.DATABASE_ERROR);
        }
        return userVO;
    }

    /**
     * search for all the user details from the database.
     *
     * @param searchUser UserVO
     * @return List of users
     * @throws DataAccessException on error
     */
    @SuppressWarnings("unchecked")
    public List<UserVO> searchAllUsers(final UserVO searchUser) throws DataAccessException {
        CriteriaBuilder builder = em.unwrap(Session.class).getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot);

        if (!StringUtils.isEmpty(searchUser.getName())) {
            String pattern = searchPattern(searchUser.getIncludes(), searchUser.getStartsWith(),
                    searchUser.getName());
            criteria.where(builder.like(userRoot.get("name"), pattern));
        }

        if (!StringUtils.isEmpty(searchUser.getLoginId())) {
            String pattern = searchPattern(searchUser.getIncludes(), searchUser.getStartsWith(),
                    searchUser.getLoginId());
            criteria.where(builder.like(userRoot.get("logInId"), pattern));
        }

        if (!StringUtils.isEmpty(searchUser.getRole())) {
            String pattern = searchPattern(searchUser.getIncludes(), searchUser.getStartsWith(),
                    searchUser.getRole());
            criteria.where(builder.equal(userRoot.get("role"), pattern));
        }
        List<User> resultUsers = em.unwrap(Session.class).createQuery(criteria).getResultList();
        return convertUsersToUserVOs(resultUsers);
    }

    private String searchPattern(final boolean includes, final boolean startsWith, final String field) {
        String pattern;
        if (includes) {
            pattern = "%" + field + "%";
        } else if (startsWith) {
            pattern = field + "%";
        } else {
            pattern = field;
        }
        return pattern;
    }

    /**
     * updateUser.
     *
     * @param userVO user
     */
    @Override
    public void updateUser(final UserVO userVO) {
        Optional<User> optionalUser = userRepository.findById(userVO.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(userVO.getName());
            user.setLogInId(userVO.getLoginId());
            user.setPassword(userVO.getPassword());
            user.setRole(userVO.getRole());
            user.setModifiedBy(userVO.getLastModifiedBy());
        }
    }


    /**
     * save to add a new user.
     *
     * @param userVO user
     * @throws UserException on error
     */
    public void save(final UserVO userVO) throws UserException {
        User user = convertToUser(userVO);
        try {
            userRepository.save(user);
        } catch (DataAccessException ex) {
            LOG.error(ex.getLocalizedMessage());
            throw new UserException(UserException.DATABASE_ERROR);
        }
    }

    private User convertToUser(final UserVO userVO) {
        User user = new User();
        user.setName(userVO.getName());
        user.setLogInId(userVO.getLoginId());
        user.setPassword(userVO.getPassword());
        user.setRole(userVO.getRole());
        user.setCreatedBy(userVO.getCreatedBy());
        user.setModifiedBy(userVO.getLastModifiedBy());
        return user;
    }


    /**
     * delete user.
     *
     * @param id id
     */
    @Override
    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    /**
     * search for a list of users.
     *
     * @param searchUser UserVO
     * @return List of User
     * @throws UserException on error
     */
    public List<UserVO> searchUserDetails(final UserVO searchUser) throws UserException {
        List<UserVO> userList;
        try {
            userList = searchAllUsers(searchUser);
        } catch (DataAccessException ex) {
            throw new UserException(UserException.DATABASE_ERROR);
        }
        return userList;
    }

    @Override
    public UserVO findByUsername(final String username) {
        User user = userRepository.findByName(username);
        return convertTOUserVO(user);
    }

    private UserVO convertTOUserVO(final User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getUserId());
        userVO.setName(user.getName());
        userVO.setLoginId(user.getLogInId());
        userVO.setPassword(user.getPassword());
        userVO.setRole(user.getRole());
        userVO.setCreatedBy(user.getCreatedBy());
        userVO.setLastModifiedBy(user.getModifiedBy());
        return userVO;
    }

    /**
     * Row mapper as inner class.
     */
    private class UserRowMapper implements RowMapper {

        /**
         * method to map the result to vo.
         *
         * @param resultSet resultSet instance
         * @param instance         i instance
         * @return UserVO as Object
         * @throws SQLException on error
         */
        public Object mapRow(final ResultSet resultSet, final int instance) throws SQLException {
            UserVO user = new UserVO();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setLoginId(resultSet.getString("LogId"));
            user.setPassword(resultSet.getString("Pass"));
            user.setRole(resultSet.getString("role"));
            user.setCreatedBy(resultSet.getString("createdBy"));
            user.setCreatedDate(resultSet.getDate("createdOn"));
            user.setLastModifiedBy(resultSet.getString("modifiedBy"));
            user.setModifiedDate(resultSet.getDate("modifiedOn"));
            return user;
        }
    }
}
