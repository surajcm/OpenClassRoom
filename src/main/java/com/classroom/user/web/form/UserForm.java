package com.classroom.user.web.form;

import com.classroom.user.domain.UserVO;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class UserForm {
    private Long id;
    private String name;
    private String loginId;
    private String password;
    private String role;
    private String message;
    private UserVO user;
    private Collection<UserVO> userVOs;
    private String loggedInUser;
    private String loggedInRole;
    private UserVO searchUser;
    private String statusMessage;
    private String statusMessageType;
    private List<String> roleList;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(final String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(final UserVO user) {
        this.user = user;
    }

    public Collection<UserVO> getUserVOs() {
        return userVOs;
    }

    public void setUserVOs(final Collection<UserVO> userVOs) {
        this.userVOs = userVOs;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(final String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String getLoggedInRole() {
        return loggedInRole;
    }

    public void setLoggedInRole(final String loggedInRole) {
        this.loggedInRole = loggedInRole;
    }

    public UserVO getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(final UserVO searchUser) {
        this.searchUser = searchUser;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessageType() {
        return statusMessageType;
    }

    public void setStatusMessageType(final String statusMessageType) {
        this.statusMessageType = statusMessageType;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(final List<String> roleList) {
        this.roleList = roleList;
    }

    /**
     * for an equivalent vo of form.
     *
     * @return user vo instance
     */
    public UserVO getCurrentUser() {
        UserVO userVO = new UserVO();
        userVO.setEmail(getLoginId());
        userVO.setPassword(getPassword());
        userVO.setRole(getRole());
        return userVO;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", message='" + message + '\'' +
                ", user=" + user +
                ", userVOs=" + userVOs +
                ", loggedInUser='" + loggedInUser + '\'' +
                ", loggedInRole='" + loggedInRole + '\'' +
                ", searchUser=" + searchUser +
                '}';
    }
}
