package com.classroom.user.dao.impl

import com.classroom.user.dao.impl.entities.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long>