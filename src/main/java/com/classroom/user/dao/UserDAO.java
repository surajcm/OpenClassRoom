package com.classroom.user.dao;

import com.classroom.user.domain.UserVO;
import com.classroom.user.exception.UserException;

import java.util.List;

@SuppressWarnings("unused")
public interface UserDAO {

    /**
     * log in dao.
     *
     * @param user user
     * @return User instance from database
     * @throws UserException on error
     */
    UserVO logIn(UserVO user) throws UserException;

    /**
     * getAllUserDetails to list all user details.
     *
     * @return List of User
     * @throws UserException on error
     */
    List<UserVO> getAllUserDetails() throws UserException;

    /**
     * create new user.
     *
     * @param user user
     * @throws UserException on error
     */
    void addNewUser(UserVO user) throws UserException;

    /**
     * getUserDetailsFromID to get the single user details from its id.
     *
     * @param id id
     * @return UserVO
     * @throws UserException on error
     */
    UserVO getUserDetailsFromID(Long id) throws UserException;

    /**
     * updates the current user details.
     *
     * @param user user
     * @throws UserException on error
     */
    void updateUser(UserVO user) throws UserException;

    /**
     * deletes the selected user.
     *
     * @param id id of the user
     * @throws UserException on error
     */
    void deleteUser(Long id) throws UserException;

    /**
     * search for a list of users.
     *
     * @param searchUser UserVO
     * @return List of User
     * @throws UserException on error
     */
    List<UserVO> searchUserDetails(UserVO searchUser) throws UserException;

    UserVO findByUsername(String username);
}
