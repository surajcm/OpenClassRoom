package com.classroom.user.service.impl

import com.classroom.user.dao.UserDAO
import org.apache.catalina.realm.UserDatabaseRealm.getRoles
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class UserDetailsServiceImpl(private val userRepository: UserDAO): UserDetailsService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String?): UserDetails? {
        logger.info(" user name  is {}", email)
        val user = userRepository.findByEmail(email!!)
        val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet()
        if (user != null) {
            user.roles.forEach { r -> grantedAuthorities.add(SimpleGrantedAuthority(r.name)) }
        } else {
            throw UsernameNotFoundException("User not found with email: $email")
        }
        return User(
            user.email,
            user.password, grantedAuthorities
        )
    }
}