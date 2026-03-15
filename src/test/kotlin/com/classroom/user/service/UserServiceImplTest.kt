package com.classroom.user.service

import com.classroom.TestDataFactory
import com.classroom.user.dao.UserDAO
import com.classroom.user.service.impl.UserServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceImplTest {

    private lateinit var userDAO: UserDAO
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userService: UserServiceImpl

    @BeforeEach
    fun setup() {
        userDAO = mockk()
        passwordEncoder = mockk()
        userService = UserServiceImpl(userDAO, passwordEncoder)
    }

    @Test
    fun `should encode password when saving new user`() {
        // Given
        val plainPassword = "password123"
        val encodedPassword = "encoded_password123"
        val user = TestDataFactory.createUser(id = null, password = plainPassword)
        val savedUser = TestDataFactory.createUser(id = 1L, password = encodedPassword)

        every { passwordEncoder.encode(plainPassword) } returns encodedPassword
        every { userDAO.save(any()) } returns savedUser

        // When
        val result = userService.save(user)

        // Then
        verify { passwordEncoder.encode(plainPassword) }
        verify { userDAO.save(match { it.password == encodedPassword }) }
        assertThat(result.password).isEqualTo(encodedPassword)
    }

    @Test
    fun `should encode password when updating user with new password`() {
        // Given
        val userId = 1L
        val newPassword = "newPassword456"
        val encodedNewPassword = "encoded_newPassword456"
        val existingUser = TestDataFactory.createUser(id = userId, password = "oldEncodedPassword")
        val userToUpdate = TestDataFactory.createUser(id = userId, password = newPassword)
        val updatedUser = TestDataFactory.createUser(id = userId, password = encodedNewPassword)

        every { userDAO.findById(userId) } returns existingUser
        every { passwordEncoder.encode(newPassword) } returns encodedNewPassword
        every { userDAO.save(any()) } returns updatedUser

        // When
        val result = userService.save(userToUpdate)

        // Then
        verify { userDAO.findById(userId) }
        verify { passwordEncoder.encode(newPassword) }
        assertThat(result.password).isEqualTo(encodedNewPassword)
    }

    @Test
    fun `should keep existing password when updating user with blank password`() {
        // Given
        val userId = 1L
        val existingPassword = "existingEncodedPassword"
        val existingUser = TestDataFactory.createUser(id = userId, password = existingPassword)
        val userToUpdate = TestDataFactory.createUser(id = userId, password = "")
        val updatedUser = TestDataFactory.createUser(id = userId, password = existingPassword)

        every { userDAO.findById(userId) } returns existingUser
        every { userDAO.save(any()) } returns updatedUser

        // When
        val result = userService.save(userToUpdate)

        // Then
        verify { userDAO.findById(userId) }
        verify(exactly = 0) { passwordEncoder.encode(any()) }
        assertThat(result.password).isEqualTo(existingPassword)
    }

    @Test
    fun `should return false when checking email uniqueness for new user with existing email`() {
        // Given
        val email = "existing@example.com"
        val existingUser = TestDataFactory.createUser(id = 1L, email = email)

        every { userDAO.findByEmail(email) } returns existingUser

        // When
        val result = userService.isEmailUnique(null, email)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when checking email uniqueness for new user with unique email`() {
        // Given
        val email = "unique@example.com"

        every { userDAO.findByEmail(email) } returns null

        // When
        val result = userService.isEmailUnique(null, email)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return true when checking email uniqueness for existing user with same email`() {
        // Given
        val userId = 1L
        val email = "user@example.com"
        val existingUser = TestDataFactory.createUser(id = userId, email = email)

        every { userDAO.findByEmail(email) } returns existingUser

        // When
        val result = userService.isEmailUnique(userId, email)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when checking email uniqueness for existing user with different existing email`() {
        // Given
        val userId = 1L
        val email = "another@example.com"
        val anotherUser = TestDataFactory.createUser(id = 2L, email = email)

        every { userDAO.findByEmail(email) } returns anotherUser

        // When
        val result = userService.isEmailUnique(userId, email)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should delegate getAllUserDetails to DAO`() {
        // Given
        val pageNumber = 1
        val users = TestDataFactory.createMultipleUsers(5)
        val page = PageImpl(users)

        every { userDAO.getAllUserDetails(pageNumber) } returns page

        // When
        val result = userService.getAllUserDetails(pageNumber)

        // Then
        verify { userDAO.getAllUserDetails(pageNumber) }
        assertThat(result).isEqualTo(page)
    }

    @Test
    fun `should delegate searchUserDetails to DAO`() {
        // Given
        val searchUser = TestDataFactory.createUser()
        val users = listOf(searchUser)

        every { userDAO.searchUserDetails(searchUser, true, false) } returns users

        // When
        val result = userService.searchUserDetails(searchUser, true, false)

        // Then
        verify { userDAO.searchUserDetails(searchUser, true, false) }
        assertThat(result).isEqualTo(users)
    }

    @Test
    fun `should delegate listRoles to DAO`() {
        // Given
        val roles = listOf(TestDataFactory.createRole())

        every { userDAO.listRoles() } returns roles

        // When
        val result = userService.listRoles()

        // Then
        verify { userDAO.listRoles() }
        assertThat(result).isEqualTo(roles)
    }

    @Test
    fun `should delegate getUserById to DAO`() {
        // Given
        val userId = 1L
        val user = TestDataFactory.createUser(id = userId)

        every { userDAO.findById(userId) } returns user

        // When
        val result = userService.getUserById(userId)

        // Then
        verify { userDAO.findById(userId) }
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `should delegate delete to DAO`() {
        // Given
        val userId = 1L

        every { userDAO.delete(userId) } returns Unit

        // When
        userService.delete(userId)

        // Then
        verify { userDAO.delete(userId) }
    }

    @Test
    fun `should delegate updateUserEnabledStatus to DAO`() {
        // Given
        val userId = 1L
        val status = false

        every { userDAO.updateUserEnabledStatus(userId, status) } returns Unit

        // When
        userService.updateUserEnabledStatus(userId, status)

        // Then
        verify { userDAO.updateUserEnabledStatus(userId, status) }
    }
}
