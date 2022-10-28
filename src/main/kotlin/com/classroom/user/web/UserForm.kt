package com.classroom.user.web

import com.classroom.user.domain.UserVO

@SuppressWarnings("unused")
class UserForm {
    private var id: Long? = null
    private var name: String? = null
    private var loginId: String? = null
    private var password: String? = null
    private var role: String? = null
    private var message: String? = null
    private var user: UserVO? = null
    private var userVOs: Collection<UserVO?>? = null
    private var loggedInUser: String? = null
    private var loggedInRole: String? = null
    private var searchUser: UserVO? = null
    private var statusMessage: String? = null
    private var statusMessageType: String? = null
    private var roleList: List<String?>? = null

    fun getId(): Long? {
        return id
    }

    fun setId(id: Long?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getLoginId(): String? {
        return loginId
    }

    fun setLoginId(loginId: String?) {
        this.loginId = loginId
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getRole(): String? {
        return role
    }

    fun setRole(role: String?) {
        this.role = role
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getUser(): UserVO? {
        return user
    }

    fun setUser(user: UserVO?) {
        this.user = user
    }

    fun getUserVOs(): Collection<UserVO?>? {
        return userVOs
    }

    fun setUserVOs(userVOs: Collection<UserVO?>?) {
        this.userVOs = userVOs
    }

    fun getLoggedInUser(): String? {
        return loggedInUser
    }

    fun setLoggedInUser(loggedInUser: String?) {
        this.loggedInUser = loggedInUser
    }

    fun getLoggedInRole(): String? {
        return loggedInRole
    }

    fun setLoggedInRole(loggedInRole: String?) {
        this.loggedInRole = loggedInRole
    }

    fun getSearchUser(): UserVO? {
        return searchUser
    }

    fun setSearchUser(searchUser: UserVO?) {
        this.searchUser = searchUser
    }

    fun getStatusMessage(): String? {
        return statusMessage
    }

    fun setStatusMessage(statusMessage: String?) {
        this.statusMessage = statusMessage
    }

    fun getStatusMessageType(): String? {
        return statusMessageType
    }

    fun setStatusMessageType(statusMessageType: String?) {
        this.statusMessageType = statusMessageType
    }

    fun getRoleList(): List<String?>? {
        return roleList
    }

    fun setRoleList(roleList: List<String?>?) {
        this.roleList = roleList
    }

    /**
     * for an equivalent vo of form.
     *
     * @return user vo instance
     */
    fun getCurrentUser(): UserVO? {
        val userVO = UserVO()
        userVO.email = getLoginId()
        userVO.password = getPassword()
        userVO.role = getRole()
        return userVO
    }

    override fun toString(): String {
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
                '}'
    }
}