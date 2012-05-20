package com.classRoom.User.web.form;

import com.classRoom.User.domain.UserVO;

import java.util.Collection;

/**
 * @author : Suraj Muraleedharan
 * Date: Feb 1, 2011
 * Time: 10:21:09 PM
 */
@SuppressWarnings("unused")
public class UserForm {
    private Long id;
	private String name;
	private String password;
	private String role;
	private String message;
    private UserVO user;
    private Collection<UserVO> userVOs;
    private String loggedInUser;
    private String loggedInRole;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserVO getUser() {
        return user;
    }

    /**
     * for equivalent vo of form
     * @return user vo instance
     */
    public UserVO getCurrentUser() {
        UserVO userVO = new UserVO();
        userVO.setName(getName());
        userVO.setPassword(getPassword());
        userVO.setRole(getRole());
        return userVO;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public Collection<UserVO> getUserVOs() {
        return userVOs;
    }

    public void setUserVOs(Collection<UserVO> userVOs) {
        this.userVOs = userVOs;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String getLoggedInRole() {
        return loggedInRole;
    }

    public void setLoggedInRole(String loggedInRole) {
        this.loggedInRole = loggedInRole;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserForm");
        sb.append("{id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", user=").append(user);
        sb.append(", userVOs=").append(userVOs);
        sb.append(", loggedInUser='").append(loggedInUser).append('\'');
        sb.append(", loggedInRole='").append(loggedInRole).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
