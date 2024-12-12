package com.classroom.user.dao.impl

import com.classroom.user.dao.impl.entities.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: CrudRepository<Role, Long>