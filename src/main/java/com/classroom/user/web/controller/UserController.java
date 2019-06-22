package com.classroom.user.web.controller;

import com.classroom.user.delegate.UserDelegate;
import com.classroom.user.domain.UserVO;
import com.classroom.user.exception.UserException;
import com.classroom.user.web.form.UserForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : Suraj Muraleedharan
 *         Date: Nov 27, 2010
 *         Time: 2:38:15 PM
 */
@Controller
public class UserController {

    /**
     * user Delegate instance
     */
    private UserDelegate userDelegate;

    /**
     * logger for user controller
     */
    private final Log log = LogFactory.getLog(UserController.class);

    /**
     * getUserDelegate
     *
     * @return UserDelegate
     */
    @SuppressWarnings("unused")
    public UserDelegate getUserDelegate() {
        return userDelegate;
    }

    /**
     * spring setter for user delegate
     *
     * @param userDelegate userDelegate instance
     */
    @SuppressWarnings("unused")
    public void setUserDelegate(UserDelegate userDelegate) {
        this.userDelegate = userDelegate;
    }

    @GetMapping(value = {"/", "/welcome"})
    public String welcome(Model model) {
        log.info("received incoming traffic and redirected to welcome");
        return "welcome";
    }

    @GetMapping(value = "/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    /**
     * Used to list all users (can be done only by admin user)
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm userForm instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView ListAll(HttpServletRequest request,
                                HttpServletResponse response, UserForm userForm) {
        log.info(" Inside ListAll method of User Controller ");
        List<UserVO> userList = null;
        try {
            userList = getUserDelegate().getAllUserDetails();
        } catch (UserException e) {
            userForm.setStatusMessage("Unable to list the Users due to an error");
            userForm.setStatusMessageType("error");
            e.printStackTrace();
            log.error(" Exception type in controller " + e.ExceptionType);
            if (e.getExceptionType().equalsIgnoreCase(UserException.DATABASE_ERROR)) {
                log.info(" An error occurred while fetching data from database. !! ");
            } else {
                log.info(" An Unknown Error has been occurred !!");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            log.info(" An Unknown Error has been occurred !!");

        }
        if (userList != null) {
            for (UserVO userIteration : userList) {
                log.info(" User detail " + userIteration.toString());
            }
        }
        userForm.setUserVOs(userList);
        userForm.setLoggedInUser(userForm.getLoggedInUser());
        userForm.setLoggedInRole(userForm.getLoggedInRole());
        userForm.setSearchUser(new UserVO());
        userForm.setRoleList(populateRoles());
        return new ModelAndView("user/UserList", "userForm", userForm);
    }

    private List<String> populateRoles() {
        List<String> roleList = new ArrayList<>();
        roleList.add("ADMIN");
        roleList.add("TEACHER");
        roleList.add("STUDENT");
        return roleList;
    }

    /**
     * Screen to add a new user
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm userForm instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView AddUser(HttpServletRequest request,
                                HttpServletResponse response, UserForm userForm) {
        log.info(" Inside AddUser method of User Controller ");
        userForm.setLoggedInUser(userForm.getLoggedInUser());
        userForm.setLoggedInRole(userForm.getLoggedInRole());
        userForm.setUser(new UserVO());
        userForm.setRoleList(populateRoles());
        return new ModelAndView("user/userAdd", "userForm", userForm);
    }

    /**
     * add a new user to database
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm user instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView SaveUser(HttpServletRequest request,
                                 HttpServletResponse response, UserForm userForm) {
        log.info(" Inside SaveUser method of User Controller ");
        log.info(" User instance to add to database " + userForm.toString());
        try {
            userForm.getUser().setCreatedDate(new Date());
            userForm.getUser().setModifiedDate(new Date());
            userForm.getUser().setCreatedBy(userForm.getLoggedInUser());
            userForm.getUser().setLastModifiedBy(userForm.getLoggedInUser());
            getUserDelegate().addNewUser(userForm.getUser());
            userForm.setStatusMessage("Successfully saved the new User");
            userForm.setStatusMessageType("success");
        } catch (UserException e) {
            userForm.setStatusMessage("Unable to save the User due to a database error");
            userForm.setStatusMessageType("error");
            e.printStackTrace();
            log.error(" Exception type in controller " + e.ExceptionType);
            if (e.getExceptionType().equalsIgnoreCase(UserException.DATABASE_ERROR)) {
                log.info(" An error occurred while fetching data from database. !! ");
            } else {
                log.info(" An Unknown Error has been occurred !!");
            }

        } catch (Exception e1) {
            userForm.setStatusMessage("Unable to save the User due to an error");
            userForm.setStatusMessageType("error");
            e1.printStackTrace();
            log.info(" An Unknown Error has been occurred !!");

        }
        List<UserVO> userList = null;
        try {
            userList = getUserDelegate().getAllUserDetails();
        } catch (UserException e) {
            userForm.setStatusMessage("Unable to list the Users due to an error");
            userForm.setStatusMessageType("error");
            e.printStackTrace();
            log.error(" Exception type in controller " + e.ExceptionType);
            if (e.getExceptionType().equalsIgnoreCase(UserException.DATABASE_ERROR)) {
                log.info(" An error occurred while fetching data from database. !! ");
            } else {
                log.info(" An Unknown Error has been occurred !!");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            log.info(" An Unknown Error has been occurred !!");

        }
        if (userList != null) {
            for (UserVO userIteration : userList) {
                log.info(" User detail " + userIteration.toString());
            }
        }
        userForm.setUserVOs(userList);
        userForm.setLoggedInUser(userForm.getLoggedInUser());
        userForm.setLoggedInRole(userForm.getLoggedInRole());
        userForm.setSearchUser(new UserVO());
        userForm.setRoleList(populateRoles());
        return new ModelAndView("user/UserList", "userForm", userForm);
    }

    /**
     * Screen to add a new user
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm userForm instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView EditUser(HttpServletRequest request,
                                 HttpServletResponse response, UserForm userForm) {
        log.info(" Inside EditUser method of User Controller ");
        log.info(" user is " + userForm.toString());
        UserVO userVO = null;
        try {
            userVO = getUserDelegate().getUserDetailsFromID(userForm.getId());
        } catch (UserException e) {
            e.printStackTrace();
            log.error(" Exception type in controller " + e.ExceptionType);
            if (e.getExceptionType().equalsIgnoreCase(UserException.DATABASE_ERROR)) {
                log.info(" An error occurred while fetching data from database. !! ");
            } else {
                log.info(" An Unknown Error has been occurred !!");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            log.info(" An Unknown Error has been occurred !!");
        }
        if (userVO == null) {
            log.error(" No details found for current user !!");
        } else {
            log.info(" User details are " + userVO);
        }
        userForm.setUser(userVO);
        userForm.setLoggedInUser(userForm.getLoggedInUser());
        userForm.setLoggedInRole(userForm.getLoggedInRole());
        userForm.setRoleList(populateRoles());
        return new ModelAndView("user/userEdit", "userForm", userForm);
    }

    /**
     * updates the user
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm user instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView UpdateUser(HttpServletRequest request,
                                   HttpServletResponse response, UserForm userForm) {
        log.info(" Inside UpdateUser method of User Controller ");
        try {
            userForm.getUser().setLastModifiedBy(userForm.getLoggedInUser());
            userForm.getUser().setModifiedDate(new Date());
            log.info(" User instance to update " + userForm.getUser().toString());
            getUserDelegate().UpdateUser(userForm.getUser());
            userForm.setStatusMessage("Successfully updated the User");
            userForm.setStatusMessageType("success");
        } catch (UserException e) {
            userForm.setStatusMessage("Unable to update the User due to a database error");
            userForm.setStatusMessageType("error");
            e.printStackTrace();
            log.error(" Exception type in controller " + e.ExceptionType);
            if (e.getExceptionType().equalsIgnoreCase(UserException.DATABASE_ERROR)) {
                log.info(" An error occurred while fetching data from database. !! ");
            } else {
                log.info(" An Unknown Error has been occurred !!");
            }

        } catch (Exception e1) {
            userForm.setStatusMessage("Unable to update the User due to an error");
            userForm.setStatusMessageType("error");
            e1.printStackTrace();
            log.info(" An Unknown Error has been occurred !!");

        }
        return ListAll(request, response, userForm);
    }

    /**
     * delete the user
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm userForm instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView DeleteUser(HttpServletRequest request,
                                   HttpServletResponse response, UserForm userForm) {
        log.info(" Inside DeleteUser method of User Controller ");
        log.info(" user is " + userForm.toString());
        try {
            getUserDelegate().deleteUser(userForm.getId());
            userForm.setStatusMessage("Successfully deleted the User");
            userForm.setStatusMessageType("success");
        } catch (UserException e) {
            userForm.setStatusMessage("Unable to delete the User due to a database error");
            userForm.setStatusMessageType("error");
            e.printStackTrace();
            log.error(" Exception type in controller " + e.ExceptionType);
            if (e.getExceptionType().equalsIgnoreCase(UserException.DATABASE_ERROR)) {
                log.info(" An error occurred while fetching data from database. !! ");
            } else {
                log.info(" An Unknown Error has been occurred !!");
            }

        } catch (Exception e1) {
            userForm.setStatusMessage("Unable to delete the User due to an error");
            userForm.setStatusMessageType("error");
            e1.printStackTrace();
            log.info(" An Unknown Error has been occurred !!");

        }

        return ListAll(request, response, userForm);
    }

    /**
     * Screen to home
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm userForm instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView ToHome(HttpServletRequest request,
                               HttpServletResponse response, UserForm userForm) {
        log.info(" Inside ToHome method of User Controller ");
        userForm.setLoggedInUser(userForm.getLoggedInUser());
        userForm.setLoggedInRole(userForm.getLoggedInRole());
        return new ModelAndView("MainPage", "userForm", userForm);
    }

    /**
     * Screen to home
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm userForm instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView LogMeOut(HttpServletRequest request,
                                 HttpServletResponse response,
                                 UserForm userForm) {
        log.info(" Inside LogMeOut method of User Controller ");

        return new ModelAndView("user/logIn", "userForm", new UserForm());
    }

    /**
     * Screen to search for a user
     *
     * @param request  HttpServletRequest instance
     * @param response HttpServletResponse instance
     * @param userForm userForm instance
     * @return ModelAndView to render
     */
    @SuppressWarnings("unused")
    public ModelAndView SearchUser(HttpServletRequest request,
                                   HttpServletResponse response,
                                   UserForm userForm) {
        log.info(" Inside SearchUser method of User Controller ");
        log.info(" User Details are " + userForm.getSearchUser().toString());
        List<UserVO> userList = null;
        try {
            userList = getUserDelegate().searchUser(userForm.getSearchUser());
            if(userList != null && userList.size()> 0){
                userForm.setStatusMessage("Successfully fetched "+userList.size()+ " Users");
                userForm.setStatusMessageType("info");
            }
        } catch (UserException e) {
            userForm.setStatusMessage("Unable to search due to a database error");
            userForm.setStatusMessageType("error");
            e.printStackTrace();
            log.error(" Exception type in controller " + e.ExceptionType);
            if (e.getExceptionType().equalsIgnoreCase(UserException.DATABASE_ERROR)) {
                log.info(" An error occurred while fetching data from database. !! ");
            } else {
                log.info(" An Unknown Error has been occurred !!");
            }

        } catch (Exception e1) {
            userForm.setStatusMessage("Unable to search due to an error");
            userForm.setStatusMessageType("error");
            e1.printStackTrace();
            log.info(" An Unknown Error has been occurred !!");

        }
        if (userList != null) {
            for (UserVO userIteration : userList) {
                log.info(" User detail " + userIteration.toString());
            }
        }
        userForm.setUserVOs(userList);
        userForm.setRoleList(populateRoles());
        return new ModelAndView("user/UserList", "userForm", userForm);
    }

}
