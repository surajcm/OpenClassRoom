package com.classroom.user.service.impl

import com.classroom.user.dao.UserDAO
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(private val userDAO: UserDAO) : UserDetailsService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun loadUserByUsername(email: String): UserDetails {
        logger.info("Loading user by email: {}", email)

        val user = userDAO.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found with email: $email")

        val authorities = user.roles.map { SimpleGrantedAuthority(it.name) }

        return User(user.email, user.password, authorities)
    }
}