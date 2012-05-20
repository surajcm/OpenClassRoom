package com.classRoom.User.dao.impl;

import com.classRoom.User.dao.UserDAO;
import com.classRoom.User.domain.UserVO;
import com.classRoom.User.exception.UserException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author : Suraj Muraleedharan
 * Date: Nov 27, 2010
 * Time: 12:43:13 PM
 */
public class UserDAOImpl extends JdbcDaoSupport implements UserDAO {

    //logger
    private final Log log = LogFactory.getLog(UserDAOImpl.class);

    //select all from table
    private static final String GET_ALL_USERS_SQL = " select * from user ";

    // select single user information from database
    private static final String GET_SINGLE_USER_SQL = " select * from user where id = ? ";

    // get user details by name
    private static final String GET_USER_BY_NAME_SQL = " select * from user where name = ? ";

    // update user details
    private static final String UPDATE_USER_SQL = " update user set name = ?, pass = ?, role = ?, modifiedOn = ? , modifiedBy = ? where id = ?";

    // insert new user
    private static final String INSERT_USER_SQL = " insert into user(name, pass , role) values (?, ? ,?)";

    // delete user details by id
    private static final String DELETE_BY_ID_SQL = " delete from user where id = ? ";

    /**
     * log in
     *
     * @param user user
     * @return User instance from database
     * @throws UserException on error
     */
    public UserVO logIn(UserVO user) throws UserException {
        UserVO currentUser;
        try {
            currentUser = getUserByName(user.getName());
        } catch (DataAccessException e) {
            throw new UserException(UserException.DATABASE_ERROR);
        }

        if (currentUser != null) {
            log.info(" User details fetched successfully,for user name " + user.getName());
            // check whether the password given is correct or not
            if (!user.getPassword().equalsIgnoreCase(currentUser.getPassword())) {
                throw new UserException(UserException.INCORRECT_PASSWORD);
            }
            log.info(" Password matched successfully, user details are " + currentUser.toString());
        } else {
            // invalid user
            throw new UserException(UserException.UNKNOWN_USER);
        }
        return currentUser;
    }

    /**
     * getAllUserDetails to list all user details
     *
     * @return List of User
     * @throws UserException
     */
    public List<UserVO> getAllUserDetails() throws UserException {
        List<UserVO> userList;
        try {
            userList = getAllUsers();
        } catch (DataAccessException e) {
            throw new UserException(UserException.DATABASE_ERROR);
        }
        return userList;
    }

    /**
     * addNewUser to add a new user
     *
     * @param user user
     * @throws UserException on error
     */
    public void addNewUser(UserVO user) throws UserException {
        try {
            saveUser(user);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new UserException(UserException.DATABASE_ERROR);
        }
    }

    /**
     * getUserDetailsFromID to get the single user details from its id
     *
     * @param id id
     * @return UserVO
     * @throws UserException on error
     */
    public UserVO getUserDetailsFromID(Long id) throws UserException {
        UserVO userVO;
        try {
            userVO = getUserById(id);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new UserException(UserException.DATABASE_ERROR);
        }
        return userVO;
    }


    /**
     * get all the user details from the database
     *
     * @return List of users
     * @throws DataAccessException on error
     */
    @SuppressWarnings("unchecked")
    public List<UserVO> getAllUsers() throws DataAccessException {
        return getJdbcTemplate().query(GET_ALL_USERS_SQL, new UserRowMapper());
    }

    /**
     * getUserById
     *
     * @param id id
     * @return User
     */
    @SuppressWarnings("unchecked")
    public UserVO getUserById(Long id) {
        return (UserVO) getJdbcTemplate().queryForObject(GET_SINGLE_USER_SQL, new Object[]{id}, new UserRowMapper());

    }

    /**
     * getUserById
     *
     * @param name name
     * @return User
     * @throws DataAccessException on error
     */
    @SuppressWarnings("unchecked")
    public UserVO getUserByName(String name) throws DataAccessException {
        return (UserVO) getJdbcTemplate().queryForObject(GET_USER_BY_NAME_SQL, new Object[]{name}, new UserRowMapper());
    }

    /**
     * updateUser
     *
     * @param user user
     */
    public void updateUser(UserVO user) throws UserException {
        Object[] parameters = new Object[]{
                user.getName(),
                user.getPassword(),
                user.getRole(),
                user.getId(),
                new Date(),
                "admin"};

        try {
            getJdbcTemplate().update(UPDATE_USER_SQL, parameters);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new UserException(UserException.DATABASE_ERROR);
        }
    }

    /**
     * save user
     *
     * @param user user
     * @throws DataAccessException on error
     */
    public void saveUser(final UserVO user) throws DataAccessException {
        Object[] parameters =
                new Object[]{user.getName(), user.getPassword(), user.getRole()};
        getJdbcTemplate().update(INSERT_USER_SQL, parameters);

    }

    /**
     * delete user
     *
     * @param id id
     */
    @SuppressWarnings("unused")
    public void deleteUser(Long id) throws UserException {
        try {
            Object[] parameters = new Object[]{id};
            getJdbcTemplate().update(DELETE_BY_ID_SQL, parameters);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new UserException(UserException.DATABASE_ERROR);
        }
    }

    /**
     * Row mapper as inner class
     */
    private class UserRowMapper implements RowMapper {

        /**
         * method to map the result to vo
         *
         * @param resultSet resultSet instance
         * @param i         i instance
         * @return UserVO as Object
         * @throws SQLException on error
         */
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            UserVO user = new UserVO();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("pass"));
            user.setRole(resultSet.getString("role"));

            return user;
        }

    }
}
