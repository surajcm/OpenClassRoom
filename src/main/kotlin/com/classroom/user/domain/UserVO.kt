package com.classroom.user.domain

import java.util.*

class UserVO {
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var password: String? = null
    var role: String? = null
    var createdDate: Date? = null
    var modifiedDate: Date? = null
    var createdBy: String? = null
    var lastModifiedBy: String? = null
    var startsWith: Boolean = false
    var includes: Boolean = false

    override fun toString(): String {
        return "UserVO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}'
    }
}