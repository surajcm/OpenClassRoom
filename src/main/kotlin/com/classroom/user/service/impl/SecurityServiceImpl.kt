package com.classroom.user.service.impl

import com.classroom.user.service.SecurityService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class SecurityServiceImpl : SecurityService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findLoggedInUsername(): String? =
        (SecurityContextHolder.getContext().authentication?.details as? UserDetails)?.username

    fun isAuthenticated(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication != null &&
                authentication !is AnonymousAuthenticationToken &&
                authentication.isAuthenticated
    }
}