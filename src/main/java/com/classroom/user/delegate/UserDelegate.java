package com.classroom.user.delegate;

import com.classroom.user.domain.UserVO;
import com.classroom.user.exception.UserException;
import com.classroom.user.service.UserService;

import java.util.List;

@SuppressWarnings("unused")
public class UserDelegate {

    /**
     * user service instance.
     */
    private UserService userService;

    /**
     * spring setter for user service.
     *
     * @param userService userService instance
     */
    @SuppressWarnings("unused")
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    /**
     * action for log in.
     *
     * @param user user instance
     * @return User instance from database
     * @throws UserException on error
     */
    public UserVO logIn(final UserVO user) throws UserException {
        return userService.logIn(user);
    }

    /**
     * getAllUserDetails to list all user details.
     *
     * @return List of User
     * @throws UserException on error
     */
    public List<UserVO> getAllUserDetails() throws UserException {
        return userService.getAllUserDetails();
    }

    /**
     * create new user.
     *
     * @param user user
     * @throws UserException on error
     */
    public void addNewUser(final UserVO user) throws UserException {
        userService.addNewUser(user);
    }

    /**
     * getUserDetailsFromID to get the single user details from its id.
     *
     * @param id id
     * @return UserVO
     * @throws UserException on error
     */
    public UserVO getUserDetailsFromID(final Long id) throws UserException {
        return userService.getUserDetailsFromID(id);
    }

    /**
     * updates the current user.
     *
     * @param user user
     * @throws UserException on error
     */
    public void updateUser(final UserVO user) throws UserException {
        userService.updateUser(user);
    }

    /**
     * deletes the selected user.
     *
     * @param id id of the user
     * @throws UserException on error
     */
    public void deleteUser(final Long id) throws UserException {
        userService.deleteUser(id);
    }

    /**
     * search for a list of users.
     *
     * @param searchUser UserVO
     * @return List of User
     * @throws UserException on error
     */
    public List<UserVO> searchUser(final UserVO searchUser) throws UserException {
        return userService.searchUserDetails(searchUser);
    }
}
