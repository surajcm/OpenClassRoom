package com.classroom.user.domain

import com.classroom.user.dao.impl.entities.Role
import java.util.*

class UserVO {
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var password: String? = null
    var roles: MutableSet<Role> = HashSet()
    var enabled: Boolean? = null
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
                ", roles='" + roles + '\'' +
                '}'
    }
}