package com.classroom.user.service.impl;

import com.classroom.user.dao.UserDAO;
import com.classroom.user.domain.UserVO;
import com.classroom.user.exception.UserException;
import com.classroom.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final String MESSAGE = "Exception type in service impl: {} ";
    /**
     * user service instance.
     */
    private UserDAO userDAO;

    private final Log log = LogFactory.getLog(UserServiceImpl.class);

    /**
     * spring setter for user dao.
     *
     * @param userDAO userDAO instance
     */
    @SuppressWarnings("unused")
    public void setUserDAO(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * to dao layer.
     *
     * @param user user
     * @return User instance from database
     * @throws UserException on error
     */
    public UserVO logIn(final UserVO user) throws UserException {
        UserVO realUser = null;
        try {
            log.info(" Inside user service impl");
            realUser = userDAO.logIn(user);
        } catch (UserException ex) {
            log.error("Exception type in service impl : {}" , ex.getCause());
            throw new UserException(ex.getExceptionType());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return realUser;
    }

    /**
     * getAllUserDetails to list all user details.
     *
     * @return List of User
     */
    public List<UserVO> getAllUserDetails() throws UserException {
        List<UserVO> userList = null;
        try {
            userList = userDAO.getAllUserDetails();
        } catch (UserException ex) {
            log.error(MESSAGE, ex.getCause());
            throw new UserException(ex.getExceptionType());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return userList;
    }

    /**
     * create new user.
     *
     * @param user user
     * @throws UserException on error
     */
    public void addNewUser(final UserVO user) throws UserException {
        try {
            userDAO.addNewUser(user);
        } catch (UserException ex) {
            log.error(MESSAGE, ex.getCause());
            throw new UserException(ex.getExceptionType());
        } catch (Exception e1) {
            e1.printStackTrace();
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
            userVO = userDAO.getUserDetailsFromID(id);
        } catch (UserException ex) {
            log.error(MESSAGE, ex.getCause());
            throw new UserException(ex.getExceptionType());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return userVO;
    }


    /**
     * updates the current user details.
     *
     * @param user user
     * @throws UserException on error
     */
    public void updateUser(final UserVO user) throws UserException {
        try {
            userDAO.updateUser(user);
        } catch (UserException ex) {
            log.error(MESSAGE, ex.getCause());
            throw new UserException(ex.getExceptionType());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * deletes the selected user.
     *
     * @param id id of the user
     * @throws UserException on error
     */
    public void deleteUser(final Long id) throws UserException {
        try {
            userDAO.deleteUser(id);
        } catch (UserException ex) {
            log.error(MESSAGE, ex.getCause());
            throw new UserException(ex.getExceptionType());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * search for a list of users.
     *
     * @param searchUser UserVO
     * @return List of User
     * @throws UserException on error
     */
    public List<UserVO> searchUserDetails(final UserVO searchUser) throws UserException {
        List<UserVO> userList = null;
        try {
            userList = userDAO.searchUserDetails(searchUser);
        } catch (UserException ex) {
            log.error(MESSAGE, ex.getCause());
            throw new UserException(ex.getExceptionType());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return userList;
    }

}
