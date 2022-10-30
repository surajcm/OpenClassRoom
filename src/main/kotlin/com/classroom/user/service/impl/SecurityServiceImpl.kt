package com.classroom.user.service.impl

import com.classroom.user.service.SecurityService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class SecurityServiceImpl(
    private val userDetailsService: UserDetailsService,
    private val authenticationManager: AuthenticationManager): SecurityService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findLoggedInUsername(): String? {
        val userDetails = SecurityContextHolder.getContext().authentication.details
        return (userDetails as? UserDetails)?.username
    }

    override fun autologin(username: String?, password: String?) {
        logger.info("Getting login for user : {} with pass : {}", username, password)
        val userDetails = userDetailsService.loadUserByUsername(username)
        val token = UsernamePasswordAuthenticationToken(
            userDetails, password, userDetails.authorities
        )
        try {
            authenticationManager.authenticate(token)
        } catch (ex: Exception) {
            logger.error(ex.message)
        }
        if (token.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = token
            logger.debug("Auto login {} successfully!", username)
        }
    }
}