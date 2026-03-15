package com.classroom.user.service

import com.classroom.TestDataFactory
import com.classroom.user.dao.UserDAO
import com.classroom.user.service.impl.UserDetailsServiceImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.junit.jupiter.api.assertThrows

class UserDetailsServiceImplTest {

    private lateinit var userDAO: UserDAO
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @BeforeEach
    fun setup() {
        userDAO = mockk()
        userDetailsService = UserDetailsServiceImpl(userDAO)
    }

    @Test
    fun `should load user by email successfully`() {
        val email = "user@example.com"
        val password = "encodedPassword"
        val role = TestDataFactory.createRole(name = "ROLE_USER")
        val user = TestDataFactory.createUser(
            email = email,
            password = password,
            roles = mutableSetOf(role)
        )

        every { userDAO.findByEmail(email) } returns user

        val result = userDetailsService.loadUserByUsername(email)

        assertThat(result.username).isEqualTo(email)
        assertThat(result.password).isEqualTo(password)
        assertThat(result.authorities).containsExactly(SimpleGrantedAuthority("ROLE_USER"))
    }

    @Test
    fun `should throw UsernameNotFoundException when user not found`() {
        val email = "nonexistent@example.com"

        every { userDAO.findByEmail(email) } returns null

        val exception = assertThrows<UsernameNotFoundException> {
            userDetailsService.loadUserByUsername(email)
        }

        assertThat(exception.message).contains("User not found with email: $email")
    }

    @Test
    fun `should map multiple roles to granted authorities correctly`() {
        val email = "admin@example.com"
        val userRole = TestDataFactory.createRole(name = "ROLE_USER")
        val adminRole = TestDataFactory.createRole(name = "ROLE_ADMIN")
        val user = TestDataFactory.createUser(
            email = email,
            roles = mutableSetOf(userRole, adminRole)
        )

        every { userDAO.findByEmail(email) } returns user

        val result = userDetailsService.loadUserByUsername(email)

        assertThat(result.authorities).containsExactlyInAnyOrder(
            SimpleGrantedAuthority("ROLE_USER"),
            SimpleGrantedAuthority("ROLE_ADMIN")
        )
    }
}
