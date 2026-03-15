package com.classroom

import com.classroom.user.dao.impl.entities.Role
import com.classroom.user.dao.impl.entities.User

object TestDataFactory {

    fun createUser(
        id: Long? = null,
        firstName: String = "John",
        lastName: String = "Doe",
        email: String = "john.doe@example.com",
        password: String = "password123",
        enabled: Boolean = true,
        photo: String? = null,
        roles: MutableSet<Role> = mutableSetOf()
    ): User = User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password,
        enabled = enabled,
        photo = photo,
        roles = roles
    )

    fun createRole(
        id: Long? = null,
        name: String = "ROLE_USER",
        description: String = "User role"
    ): Role = Role(
        id = id,
        name = name,
        description = description
    )

    fun createUserWithRole(
        roleName: String = "ROLE_USER"
    ): User {
        val role = createRole(name = roleName, description = "$roleName role")
        return createUser(roles = mutableSetOf(role))
    }

    fun createMultipleUsers(
        count: Int,
        emailPrefix: String = "user"
    ): List<User> = (1..count).map { i ->
        createUser(
            id = i.toLong(),
            firstName = "User$i",
            lastName = "Test$i",
            email = "$emailPrefix$i@example.com"
        )
    }

    fun createAdminUser(): User {
        val adminRole = createRole(name = "ROLE_ADMIN", description = "Administrator role")
        return createUser(
            firstName = "Admin",
            lastName = "User",
            email = "admin@example.com",
            roles = mutableSetOf(adminRole)
        )
    }
}
