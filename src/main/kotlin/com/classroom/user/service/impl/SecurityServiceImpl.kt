package com.classroom.user.service.impl

import com.classroom.user.service.SecurityService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class SecurityServiceImpl(
    private val userDetailsService: UserDetailsService): SecurityService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findLoggedInUsername(): String? {
        val userDetails = SecurityContextHolder.getContext().authentication.details
        return (userDetails as? UserDetails)?.username
    }

    fun isAuthenticated(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null ||
            AnonymousAuthenticationToken::class.java.isAssignableFrom(authentication.javaClass)
        ) {
            return false
        }
        return authentication.isAuthenticated
    }
}