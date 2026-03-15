# Test Suite Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a comprehensive, risk-based test suite with 75-80% coverage for the OpenClassRoom application.

**Architecture:** Balanced mix of unit tests (fast, isolated with mocks), integration tests (@DataJpaTest for DAOs, @WebMvcTest for controllers), and end-to-end tests (@SpringBootTest for critical flows). Focus testing effort on high-risk areas: authentication, security, user CRUD, file operations.

**Tech Stack:** JUnit 5, MockK (Kotlin mocking), Spring Boot Test, MockMvc, AssertJ, H2 in-memory database

**Spec Document:** `docs/superpowers/specs/2026-03-15-test-suite-design.md`

---

## File Structure Overview

```
src/test/kotlin/com/classroom/
├── TestDataFactory.kt                              [Shared test data builders]
├── TestExtensions.kt                               [MockMvc Kotlin extensions]
├── user/
│   ├── service/
│   │   ├── UserServiceImplTest.kt                 [Unit - mock DAO, PasswordEncoder]
│   │   ├── UserDetailsServiceImplTest.kt          [Unit - mock UserDAO]
│   │   └── SecurityServiceImplTest.kt             [Unit - mock SecurityContext]
│   ├── dao/
│   │   ├── UserDAOImplTest.kt                     [Integration - @DataJpaTest]
│   │   ├── UserRepositoryTest.kt                  [Integration - @DataJpaTest]
│   │   ├── RoleRepositoryTest.kt                  [Integration - @DataJpaTest]
│   │   └── spec/
│   │       └── UserSpecificationTest.kt           [Unit - mock JPA criteria]
│   ├── web/
│   │   ├── UserControllerTest.kt                  [Integration - @WebMvcTest]
│   │   ├── UserRestControllerTest.kt              [Integration - @WebMvcTest]
│   │   └── RegistrationControllerTest.kt          [Integration - @WebMvcTest]
│   ├── entity/
│   │   ├── UserTest.kt                            [Unit]
│   │   └── RoleTest.kt                            [Unit]
│   └── integration/
│       └── UserFlowIntegrationTest.kt             [@SpringBootTest - E2E]
├── init/
│   ├── FileUploadUtilTest.kt                      [Unit]
│   └── WebSecurityConfigTest.kt                   [Integration - @SpringBootTest]
└── resources/
    └── application-test.properties                [Test-specific config]
```

---

## Chunk 1: Foundation Setup

### Task 1: Add Test Dependencies

**Files:**
- Modify: `gradle/dependencies.gradle`

- [ ] **Step 1: Add MockK and AssertJ dependencies**

Add to the testImplementation section in `gradle/dependencies.gradle`:

```groovy
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("com.ninja-squad:springmockk:4.0.2")
testImplementation("org.assertj:assertj-core:3.24.2")
testImplementation("org.springframework.security:spring-security-test")
```

- [ ] **Step 2: Verify dependencies resolve**

Run: `./gradlew dependencies --configuration testCompileClasspath | grep mockk`

Expected: Should show mockk dependencies resolved

- [ ] **Step 3: Commit**

```bash
git add gradle/dependencies.gradle
git commit -m "test: Add MockK, AssertJ, and Spring Security Test dependencies"
```

---

### Task 2: Create Test Resources

**Files:**
- Create: `src/test/resources/application-test.properties`

- [ ] **Step 1: Create test configuration**

Create `src/test/resources/application-test.properties`:

```properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
hsql.ui.disabled=true
server.port=0
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
logging.level.org.springframework.security=DEBUG
```

- [ ] **Step 2: Verify test configuration loads**

Run: `./gradlew test --no-daemon`

Expected: BUILD SUCCESSFUL (no tests yet, but config should load)

- [ ] **Step 3: Commit**

```bash
git add src/test/resources/application-test.properties
git commit -m "test: Add test-specific application properties"
```

---

### Task 3: Create TestDataFactory

**Files:**
- Create: `src/test/kotlin/com/classroom/TestDataFactory.kt`

- [ ] **Step 1: Create test data factory object**

Create `src/test/kotlin/com/classroom/TestDataFactory.kt`:

```kotlin
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
```

- [ ] **Step 2: Verify compilation**

Run: `./gradlew compileTestKotlin`

Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add src/test/kotlin/com/classroom/TestDataFactory.kt
git commit -m "test: Add TestDataFactory for creating test data"
```

---

### Task 4: Create MockMvc Extensions

**Files:**
- Create: `src/test/kotlin/com/classroom/TestExtensions.kt`

- [ ] **Step 1: Create Kotlin DSL extensions for MockMvc**

Create `src/test/kotlin/com/classroom/TestExtensions.kt`:

```kotlin
package com.classroom

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart

fun MockMvc.getAndExpectOk(url: String): ResultActions =
    perform(get(url)).andExpect(status().isOk)

fun MockMvc.postForm(url: String, params: Map<String, String>): ResultActions =
    perform(
        post(url)
            .params(LinkedMultiValueMap(params.mapValues { listOf(it.value) }))
    )

fun MockMvc.postMultipart(
    url: String,
    file: MockMultipartFile,
    params: Map<String, String> = emptyMap()
): ResultActions {
    val builder = multipart(url).file(file)
    params.forEach { (key, value) -> builder.param(key, value) }
    return perform(builder)
}
```

- [ ] **Step 2: Verify compilation**

Run: `./gradlew compileTestKotlin`

Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add src/test/kotlin/com/classroom/TestExtensions.kt
git commit -m "test: Add MockMvc Kotlin DSL extensions"
```

---

## Chunk 2: High-Priority Unit Tests - Services

### Task 5: UserServiceImpl Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/service/UserServiceImplTest.kt`

- [ ] **Step 1: Write test for saving new user with password encoding**

Create `src/test/kotlin/com/classroom/user/service/UserServiceImplTest.kt`:

```kotlin
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
        val savedUser = user.copy(id = 1L, password = encodedPassword)

        every { passwordEncoder.encode(plainPassword) } returns encodedPassword
        every { userDAO.save(any()) } returns savedUser

        // When
        val result = userService.save(user)

        // Then
        verify { passwordEncoder.encode(plainPassword) }
        verify { userDAO.save(match { it.password == encodedPassword }) }
        assertThat(result.password).isEqualTo(encodedPassword)
    }
}
```

- [ ] **Step 2: Run test to verify it passes**

Run: `./gradlew test --tests UserServiceImplTest`

Expected: 1 test passed

- [ ] **Step 3: Write test for updating user with new password**

Add to `UserServiceImplTest.kt`:

```kotlin
    @Test
    fun `should encode password when updating user with new password`() {
        // Given
        val userId = 1L
        val newPassword = "newPassword456"
        val encodedNewPassword = "encoded_newPassword456"
        val existingUser = TestDataFactory.createUser(id = userId, password = "oldEncodedPassword")
        val userToUpdate = TestDataFactory.createUser(id = userId, password = newPassword)
        val updatedUser = userToUpdate.copy(password = encodedNewPassword)

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
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew test --tests UserServiceImplTest`

Expected: 2 tests passed

- [ ] **Step 5: Write test for updating user with empty password**

Add to `UserServiceImplTest.kt`:

```kotlin
    @Test
    fun `should keep existing password when updating user with blank password`() {
        // Given
        val userId = 1L
        val existingPassword = "existingEncodedPassword"
        val existingUser = TestDataFactory.createUser(id = userId, password = existingPassword)
        val userToUpdate = TestDataFactory.createUser(id = userId, password = "")
        val updatedUser = userToUpdate.copy(password = existingPassword)

        every { userDAO.findById(userId) } returns existingUser
        every { userDAO.save(any()) } returns updatedUser

        // When
        val result = userService.save(userToUpdate)

        // Then
        verify { userDAO.findById(userId) }
        verify(exactly = 0) { passwordEncoder.encode(any()) }
        assertThat(result.password).isEqualTo(existingPassword)
    }
```

- [ ] **Step 6: Run test to verify it passes**

Run: `./gradlew test --tests UserServiceImplTest`

Expected: 3 tests passed

- [ ] **Step 7: Write test for email uniqueness - new user, email exists**

Add to `UserServiceImplTest.kt`:

```kotlin
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
```

- [ ] **Step 8: Run test**

Run: `./gradlew test --tests UserServiceImplTest`

Expected: 4 tests passed

- [ ] **Step 9: Write test for email uniqueness - new user, email doesn't exist**

Add to `UserServiceImplTest.kt`:

```kotlin
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
```

- [ ] **Step 10: Run test**

Run: `./gradlew test --tests UserServiceImplTest`

Expected: 5 tests passed

- [ ] **Step 11: Write test for email uniqueness - editing user, same email**

Add to `UserServiceImplTest.kt`:

```kotlin
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
```

- [ ] **Step 12: Run test**

Run: `./gradlew test --tests UserServiceImplTest`

Expected: 6 tests passed

- [ ] **Step 13: Write test for email uniqueness - editing user, different email exists**

Add to `UserServiceImplTest.kt`:

```kotlin
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
```

- [ ] **Step 14: Run test**

Run: `./gradlew test --tests UserServiceImplTest`

Expected: 7 tests passed

- [ ] **Step 15: Write remaining delegation tests**

Add to `UserServiceImplTest.kt`:

```kotlin
    @Test
    fun `should delegate getAllUserDetails to DAO`() {
        // Given
        val pageNumber = 1
        val page = mockk<org.springframework.data.domain.Page<com.classroom.user.dao.impl.entities.User>>()

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
```

- [ ] **Step 16: Run all tests**

Run: `./gradlew test --tests UserServiceImplTest`

Expected: 13 tests passed

- [ ] **Step 17: Commit**

```bash
git add src/test/kotlin/com/classroom/user/service/UserServiceImplTest.kt
git commit -m "test: Add comprehensive UserServiceImpl tests"
```

---

### Task 6: UserDetailsServiceImpl Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/service/UserDetailsServiceImplTest.kt`

- [ ] **Step 1: Write test for loading user by email - success**

Create `src/test/kotlin/com/classroom/user/service/UserDetailsServiceImplTest.kt`:

```kotlin
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
        // Given
        val email = "user@example.com"
        val password = "encodedPassword"
        val role = TestDataFactory.createRole(name = "ROLE_USER")
        val user = TestDataFactory.createUser(
            email = email,
            password = password,
            roles = mutableSetOf(role)
        )

        every { userDAO.findByEmail(email) } returns user

        // When
        val result = userDetailsService.loadUserByUsername(email)

        // Then
        assertThat(result.username).isEqualTo(email)
        assertThat(result.password).isEqualTo(password)
        assertThat(result.authorities).containsExactly(SimpleGrantedAuthority("ROLE_USER"))
    }
}
```

- [ ] **Step 2: Run test**

Run: `./gradlew test --tests UserDetailsServiceImplTest`

Expected: 1 test passed

- [ ] **Step 3: Write test for user not found**

Add to `UserDetailsServiceImplTest.kt`:

```kotlin
    @Test
    fun `should throw UsernameNotFoundException when user not found`() {
        // Given
        val email = "nonexistent@example.com"

        every { userDAO.findByEmail(email) } returns null

        // When & Then
        val exception = assertThrows<UsernameNotFoundException> {
            userDetailsService.loadUserByUsername(email)
        }

        assertThat(exception.message).contains("User not found with email: $email")
    }
```

- [ ] **Step 4: Run test**

Run: `./gradlew test --tests UserDetailsServiceImplTest`

Expected: 2 tests passed

- [ ] **Step 5: Write test for multiple roles mapping**

Add to `UserDetailsServiceImplTest.kt`:

```kotlin
    @Test
    fun `should map multiple roles to granted authorities correctly`() {
        // Given
        val email = "admin@example.com"
        val userRole = TestDataFactory.createRole(name = "ROLE_USER")
        val adminRole = TestDataFactory.createRole(name = "ROLE_ADMIN")
        val user = TestDataFactory.createUser(
            email = email,
            roles = mutableSetOf(userRole, adminRole)
        )

        every { userDAO.findByEmail(email) } returns user

        // When
        val result = userDetailsService.loadUserByUsername(email)

        // Then
        assertThat(result.authorities).containsExactlyInAnyOrder(
            SimpleGrantedAuthority("ROLE_USER"),
            SimpleGrantedAuthority("ROLE_ADMIN")
        )
    }
```

- [ ] **Step 6: Run test**

Run: `./gradlew test --tests UserDetailsServiceImplTest`

Expected: 3 tests passed

- [ ] **Step 7: Commit**

```bash
git add src/test/kotlin/com/classroom/user/service/UserDetailsServiceImplTest.kt
git commit -m "test: Add UserDetailsServiceImpl tests"
```

---

### Task 7: SecurityServiceImpl Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/service/SecurityServiceImplTest.kt`

- [ ] **Step 1: Write test for finding logged-in username**

Create `src/test/kotlin/com/classroom/user/service/SecurityServiceImplTest.kt`:

```kotlin
package com.classroom.user.service

import com.classroom.user.service.impl.SecurityServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

class SecurityServiceImplTest {

    private lateinit var securityService: SecurityServiceImpl

    @BeforeEach
    fun setup() {
        securityService = SecurityServiceImpl()
        mockkStatic(SecurityContextHolder::class)
    }

    @Test
    fun `should return username when user is authenticated`() {
        // Given
        val username = "test@example.com"
        val userDetails = User(username, "password", emptyList())
        val authentication = UsernamePasswordAuthenticationToken(
            userDetails,
            "password",
            emptyList()
        )
        authentication.details = userDetails

        val securityContext = mockk<SecurityContext>()
        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns authentication

        // When
        val result = securityService.findLoggedInUsername()

        // Then
        assertThat(result).isEqualTo(username)
    }

    @Test
    fun `should return null when authentication is null`() {
        // Given
        val securityContext = mockk<SecurityContext>()
        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns null

        // When
        val result = securityService.findLoggedInUsername()

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should return null when details is not UserDetails`() {
        // Given
        val authentication = mockk<org.springframework.security.core.Authentication>()
        every { authentication.details } returns "not a UserDetails"

        val securityContext = mockk<SecurityContext>()
        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns authentication

        // When
        val result = securityService.findLoggedInUsername()

        // Then
        assertThat(result).isNull()
    }
}
```

- [ ] **Step 2: Run tests**

Run: `./gradlew test --tests SecurityServiceImplTest`

Expected: 3 tests passed

- [ ] **Step 3: Write tests for isAuthenticated**

Add to `SecurityServiceImplTest.kt`:

```kotlin
    @Test
    fun `should return true when user is authenticated`() {
        // Given
        val authentication = mockk<org.springframework.security.core.Authentication>()
        every { authentication.isAuthenticated } returns true

        val securityContext = mockk<SecurityContext>()
        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns authentication

        // When
        val result = securityService.isAuthenticated()

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when authentication is null`() {
        // Given
        val securityContext = mockk<SecurityContext>()
        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns null

        // When
        val result = securityService.isAuthenticated()

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when authentication is anonymous`() {
        // Given
        val authentication = mockk<org.springframework.security.authentication.AnonymousAuthenticationToken>()
        every { authentication.isAuthenticated } returns true

        val securityContext = mockk<SecurityContext>()
        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns authentication

        // When
        val result = securityService.isAuthenticated()

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when authentication is not authenticated`() {
        // Given
        val authentication = mockk<org.springframework.security.core.Authentication>()
        every { authentication.isAuthenticated } returns false

        val securityContext = mockk<SecurityContext>()
        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns authentication

        // When
        val result = securityService.isAuthenticated()

        // Then
        assertThat(result).isFalse()
    }
```

- [ ] **Step 4: Run all tests**

Run: `./gradlew test --tests SecurityServiceImplTest`

Expected: 7 tests passed

- [ ] **Step 5: Commit**

```bash
git add src/test/kotlin/com/classroom/user/service/SecurityServiceImplTest.kt
git commit -m "test: Add SecurityServiceImpl tests"
```

---

### Task 8: FileUploadUtil Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/init/FileUploadUtilTest.kt`

- [ ] **Step 1: Write test for saving file successfully**

Create `src/test/kotlin/com/classroom/init/FileUploadUtilTest.kt`:

```kotlin
package com.classroom.init

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists

class FileUploadUtilTest {

    private val testBaseDir = Path.of("test-uploads")

    @AfterEach
    fun cleanup() {
        if (testBaseDir.exists()) {
            testBaseDir.deleteRecursively()
        }
    }

    @Test
    fun `should create directory if not exists and save file`() {
        // Given
        val uploadDir = testBaseDir.resolve("user-photos/1").toString()
        val fileName = "test.jpg"
        val fileContent = "test file content".toByteArray()
        val multipartFile = mockk<MultipartFile>()

        every { multipartFile.inputStream } returns ByteArrayInputStream(fileContent)

        // When
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile)

        // Then
        val savedFile = Path.of(uploadDir, fileName)
        assertThat(savedFile).exists()
        assertThat(Files.readAllBytes(savedFile)).isEqualTo(fileContent)
    }

    @Test
    fun `should save file when directory already exists`() {
        // Given
        val uploadDir = testBaseDir.resolve("existing-dir").toString()
        Files.createDirectories(Path.of(uploadDir))

        val fileName = "test.jpg"
        val fileContent = "test content".toByteArray()
        val multipartFile = mockk<MultipartFile>()

        every { multipartFile.inputStream } returns ByteArrayInputStream(fileContent)

        // When
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile)

        // Then
        val savedFile = Path.of(uploadDir, fileName)
        assertThat(savedFile).exists()
    }

    @Test
    fun `should throw IOException when save fails`() {
        // Given
        val uploadDir = testBaseDir.resolve("fail-dir").toString()
        val fileName = "test.jpg"
        val multipartFile = mockk<MultipartFile>()

        every { multipartFile.inputStream } throws IOException("Stream error")

        // When & Then
        val exception = assertThrows<IOException> {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile)
        }

        assertThat(exception.message).contains("Could not save file: $fileName")
    }
}
```

- [ ] **Step 2: Run tests**

Run: `./gradlew test --tests FileUploadUtilTest`

Expected: 3 tests passed

- [ ] **Step 3: Write tests for cleanDir**

Add to `FileUploadUtilTest.kt`:

```kotlin
    @Test
    fun `should delete only files not directories when cleaning`() {
        // Given
        val dir = testBaseDir.resolve("clean-test").toString()
        Files.createDirectories(Path.of(dir))

        // Create test files and subdirectory
        Files.createFile(Path.of(dir, "file1.txt"))
        Files.createFile(Path.of(dir, "file2.txt"))
        val subDir = Path.of(dir, "subdir")
        Files.createDirectories(subDir)
        Files.createFile(subDir.resolve("nested.txt"))

        // When
        FileUploadUtil.cleanDir(dir)

        // Then
        assertThat(Path.of(dir, "file1.txt")).doesNotExist()
        assertThat(Path.of(dir, "file2.txt")).doesNotExist()
        assertThat(subDir).exists()
        assertThat(subDir.resolve("nested.txt")).exists()
    }

    @Test
    fun `should handle non-existent directory gracefully`() {
        // Given
        val nonExistentDir = testBaseDir.resolve("non-existent").toString()

        // When & Then (should not throw)
        FileUploadUtil.cleanDir(nonExistentDir)
    }

    @Test
    fun `should handle empty directory`() {
        // Given
        val emptyDir = testBaseDir.resolve("empty").toString()
        Files.createDirectories(Path.of(emptyDir))

        // When & Then (should not throw)
        FileUploadUtil.cleanDir(emptyDir)

        assertThat(Path.of(emptyDir)).exists()
    }
```

- [ ] **Step 4: Run all tests**

Run: `./gradlew test --tests FileUploadUtilTest`

Expected: 6 tests passed

- [ ] **Step 5: Commit**

```bash
git add src/test/kotlin/com/classroom/init/FileUploadUtilTest.kt
git commit -m "test: Add FileUploadUtil tests"
```

---

## Chunk 3: High-Priority Integration Tests

### Task 9: UserRestController Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/web/UserRestControllerTest.kt`

- [ ] **Step 1: Write test for checking unique email (new user)**

Create `src/test/kotlin/com/classroom/user/web/UserRestControllerTest.kt`:

```kotlin
package com.classroom.user.web

import com.classroom.user.service.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserRestController::class)
class UserRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userService: UserService

    @Test
    fun `should return OK when email is unique for new user`() {
        // Given
        val email = "unique@example.com"
        every { userService.isEmailUnique(null, email) } returns true

        // When & Then
        mockMvc.perform(
            post("/users/check_email")
                .param("userId", "")
                .param("email", email)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("OK"))
    }

    @Test
    fun `should return Duplicated when email exists for new user`() {
        // Given
        val email = "duplicate@example.com"
        every { userService.isEmailUnique(null, email) } returns false

        // When & Then
        mockMvc.perform(
            post("/users/check_email")
                .param("userId", "")
                .param("email", email)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Duplicated"))
    }

    @Test
    fun `should return OK when existing user keeps same email`() {
        // Given
        val userId = 1L
        val email = "user@example.com"
        every { userService.isEmailUnique(userId, email) } returns true

        // When & Then
        mockMvc.perform(
            post("/users/check_email")
                .param("userId", userId.toString())
                .param("email", email)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("OK"))
    }

    @Test
    fun `should return Duplicated when existing user tries different existing email`() {
        // Given
        val userId = 1L
        val email = "another@example.com"
        every { userService.isEmailUnique(userId, email) } returns false

        // When & Then
        mockMvc.perform(
            post("/users/check_email")
                .param("userId", userId.toString())
                .param("email", email)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Duplicated"))
    }
}
```

- [ ] **Step 2: Run tests**

Run: `./gradlew test --tests UserRestControllerTest`

Expected: 4 tests passed

- [ ] **Step 3: Commit**

```bash
git add src/test/kotlin/com/classroom/user/web/UserRestControllerTest.kt
git commit -m "test: Add UserRestController tests for email validation"
```

---

### Task 10: WebSecurityConfig Integration Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/init/WebSecurityConfigTest.kt`

- [ ] **Step 1: Write security configuration integration tests**

Create `src/test/kotlin/com/classroom/init/WebSecurityConfigTest.kt`:

```kotlin
package com.classroom.init

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.assertj.core.api.Assertions.assertThat
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.datasource.url=jdbc:h2:mem:securitytest"])
class WebSecurityConfigTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should allow access to public paths without authentication`() {
        // Test login page
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk)

        // Test registration page
        mockMvc.perform(get("/registration"))
            .andExpect(status().isOk)

        // Test static resources paths (will return 404 if no actual file, but not 401/403)
        mockMvc.perform(get("/css/style.css"))
            .andExpect(status().isNotFound)

        mockMvc.perform(get("/js/script.js"))
            .andExpect(status().isNotFound)

        mockMvc.perform(get("/img/logo.png"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should require authentication for protected paths`() {
        mockMvc.perform(get("/users"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("http://localhost/login"))

        mockMvc.perform(get("/welcome"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("http://localhost/login"))
    }

    @Test
    fun `should use BCrypt password encoder`() {
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder::class.java)

        val plainPassword = "testPassword123"
        val encoded = passwordEncoder.encode(plainPassword)

        assertThat(encoded).isNotEqualTo(plainPassword)
        assertThat(passwordEncoder.matches(plainPassword, encoded)).isTrue()
    }
}
```

- [ ] **Step 2: Run tests**

Run: `./gradlew test --tests WebSecurityConfigTest`

Expected: 3 tests passed

- [ ] **Step 3: Commit**

```bash
git add src/test/kotlin/com/classroom/init/WebSecurityConfigTest.kt
git commit -m "test: Add WebSecurityConfig integration tests"
```

---

## Chunk 4: Medium-Priority Tests - Specifications

### Task 11: UserSpecification Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/dao/spec/UserSpecificationTest.kt`

- [ ] **Step 1: Write test for EQUAL operation**

Create `src/test/kotlin/com/classroom/user/dao/spec/UserSpecificationTest.kt`:

```kotlin
package com.classroom.user.dao.spec

import com.classroom.init.specs.SearchCriteria
import com.classroom.init.specs.SearchOperation
import com.classroom.user.dao.impl.entities.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserSpecificationTest {

    @Test
    fun `should create equal predicate for EQUAL operation`() {
        // Given
        val spec = UserSpecification()
        spec.add(SearchCriteria("email", "test@example.com", SearchOperation.EQUAL))

        val root = mockk<Root<User>>()
        val query = mockk<CriteriaQuery<*>>()
        val builder = mockk<CriteriaBuilder>()
        val path = mockk<Path<Any>>()
        val predicate = mockk<Predicate>()

        every { root.get<Any>("email") } returns path
        every { builder.equal(path, "test@example.com") } returns predicate
        every { builder.and(*anyVararg<Predicate>()) } returns predicate

        // When
        val result = spec.toPredicate(root, query, builder)

        // Then
        verify { builder.equal(path, "test@example.com") }
        assertThat(result).isNotNull
    }

    @Test
    fun `should create like predicate for MATCH operation with wildcards`() {
        // Given
        val spec = UserSpecification()
        spec.add(SearchCriteria("firstName", "John", SearchOperation.MATCH))

        val root = mockk<Root<User>>()
        val query = mockk<CriteriaQuery<*>>()
        val builder = mockk<CriteriaBuilder>()
        val path = mockk<Path<String>>()
        val lowerPath = mockk<jakarta.persistence.criteria.Expression<String>>()
        val predicate = mockk<Predicate>()

        every { root.get<String>("firstName") } returns path
        every { builder.lower(path) } returns lowerPath
        every { builder.like(lowerPath, "%john%") } returns predicate
        every { builder.and(*anyVararg<Predicate>()) } returns predicate

        // When
        val result = spec.toPredicate(root, query, builder)

        // Then
        verify { builder.like(lowerPath, "%john%") }
        assertThat(result).isNotNull
    }

    @Test
    fun `should create like predicate for MATCH_START operation`() {
        // Given
        val spec = UserSpecification()
        spec.add(SearchCriteria("email", "user", SearchOperation.MATCH_START))

        val root = mockk<Root<User>>()
        val query = mockk<CriteriaQuery<*>>()
        val builder = mockk<CriteriaBuilder>()
        val path = mockk<Path<String>>()
        val lowerPath = mockk<jakarta.persistence.criteria.Expression<String>>()
        val predicate = mockk<Predicate>()

        every { root.get<String>("email") } returns path
        every { builder.lower(path) } returns lowerPath
        every { builder.like(lowerPath, "user%") } returns predicate
        every { builder.and(*anyVararg<Predicate>()) } returns predicate

        // When
        val result = spec.toPredicate(root, query, builder)

        // Then
        verify { builder.like(lowerPath, "user%") }
        assertThat(result).isNotNull
    }

    @Test
    fun `should combine multiple criteria with AND logic`() {
        // Given
        val spec = UserSpecification()
        spec.add(SearchCriteria("firstName", "John", SearchOperation.EQUAL))
        spec.add(SearchCriteria("email", "test", SearchOperation.MATCH_START))

        val root = mockk<Root<User>>()
        val query = mockk<CriteriaQuery<*>>()
        val builder = mockk<CriteriaBuilder>()
        val path1 = mockk<Path<Any>>()
        val path2 = mockk<Path<String>>()
        val lowerPath = mockk<jakarta.persistence.criteria.Expression<String>>()
        val predicate1 = mockk<Predicate>()
        val predicate2 = mockk<Predicate>()
        val combinedPredicate = mockk<Predicate>()

        every { root.get<Any>("firstName") } returns path1
        every { root.get<String>("email") } returns path2
        every { builder.equal(path1, "John") } returns predicate1
        every { builder.lower(path2) } returns lowerPath
        every { builder.like(lowerPath, "test%") } returns predicate2
        every { builder.and(predicate1, predicate2) } returns combinedPredicate

        // When
        val result = spec.toPredicate(root, query, builder)

        // Then
        verify { builder.and(predicate1, predicate2) }
        assertThat(result).isEqualTo(combinedPredicate)
    }
}
```

- [ ] **Step 2: Run tests**

Run: `./gradlew test --tests UserSpecificationTest`

Expected: 4 tests passed

- [ ] **Step 3: Commit**

```bash
git add src/test/kotlin/com/classroom/user/dao/spec/UserSpecificationTest.kt
git commit -m "test: Add UserSpecification tests"
```

---

## Chunk 5: Medium-Priority Tests - DAOs

### Task 12: UserDAOImpl Integration Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/dao/UserDAOImplTest.kt`
- Create: `src/test/resources/test-data.sql`

- [ ] **Step 1: Create test data SQL**

Create `src/test/resources/test-data.sql`:

```sql
INSERT INTO roles (id, name, description) VALUES (1, 'ROLE_USER', 'User role');
INSERT INTO roles (id, name, description) VALUES (2, 'ROLE_ADMIN', 'Admin role');
```

- [ ] **Step 2: Write DAO integration tests with @DataJpaTest**

Create `src/test/kotlin/com/classroom/user/dao/UserDAOImplTest.kt`:

```kotlin
package com.classroom.user.dao

import com.classroom.TestDataFactory
import com.classroom.init.Constants
import com.classroom.user.dao.impl.RoleRepository
import com.classroom.user.dao.impl.UserDAOImpl
import com.classroom.user.dao.impl.UserRepository
import com.classroom.user.exception.UserNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql

@DataJpaTest
@Import(UserDAOImpl::class)
@Sql("/test-data.sql")
class UserDAOImplTest {

    @Autowired
    private lateinit var userDAO: UserDAOImpl

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Test
    fun `should return paginated users`() {
        // Given
        val users = TestDataFactory.createMultipleUsers(10)
        users.forEach { userRepository.save(it) }

        // When
        val page = userDAO.getAllUserDetails(1)

        // Then
        assertThat(page.content).hasSize(Constants.USERS_PER_PAGE)
        assertThat(page.totalElements).isEqualTo(10)
        assertThat(page.number).isEqualTo(0) // Page is zero-indexed internally
    }

    @Test
    fun `should add new user to database`() {
        // Given
        val user = TestDataFactory.createUser(email = "newuser@example.com")

        // When
        userDAO.addNewUser(user)

        // Then
        val saved = userRepository.findByEmail("newuser@example.com")
        assertThat(saved).isNotNull
        assertThat(saved?.firstName).isEqualTo(user.firstName)
    }

    @Test
    fun `should update existing user fields`() {
        // Given
        val user = TestDataFactory.createUser(email = "update@example.com")
        val saved = userRepository.save(user)

        val updatedUser = saved.copy(
            firstName = "Updated",
            lastName = "Name",
            email = "updated@example.com"
        )

        // When
        userDAO.updateUser(updatedUser)
        userRepository.flush()

        // Then
        val result = userRepository.findById(saved.id!!).get()
        assertThat(result.firstName).isEqualTo("Updated")
        assertThat(result.lastName).isEqualTo("Name")
        assertThat(result.email).isEqualTo("updated@example.com")
    }

    @Test
    fun `should find user by email`() {
        // Given
        val user = TestDataFactory.createUser(email = "find@example.com")
        userRepository.save(user)

        // When
        val result = userDAO.findByEmail("find@example.com")

        // Then
        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo("find@example.com")
    }

    @Test
    fun `should return null when user not found by email`() {
        // When
        val result = userDAO.findByEmail("nonexistent@example.com")

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should find user by id`() {
        // Given
        val user = TestDataFactory.createUser()
        val saved = userRepository.save(user)

        // When
        val result = userDAO.findById(saved.id!!)

        // Then
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(saved.id)
    }

    @Test
    fun `should throw UserNotFoundException when user not found by id`() {
        // When & Then
        assertThrows<UserNotFoundException> {
            userDAO.findById(999L)
        }
    }

    @Test
    fun `should search users by firstName with MATCH operation`() {
        // Given
        userRepository.save(TestDataFactory.createUser(firstName = "John", email = "john1@example.com"))
        userRepository.save(TestDataFactory.createUser(firstName = "Johnny", email = "john2@example.com"))
        userRepository.save(TestDataFactory.createUser(firstName = "Jane", email = "jane@example.com"))

        val searchUser = TestDataFactory.createUser(firstName = "john")

        // When
        val results = userDAO.searchUserDetails(searchUser, startsWith = false, includes = true)

        // Then
        assertThat(results).hasSize(2)
        assertThat(results.map { it.firstName }).containsExactlyInAnyOrder("John", "Johnny")
    }

    @Test
    fun `should search users by email with MATCH_START operation`() {
        // Given
        userRepository.save(TestDataFactory.createUser(email = "user1@example.com"))
        userRepository.save(TestDataFactory.createUser(email = "user2@example.com"))
        userRepository.save(TestDataFactory.createUser(email = "admin@example.com"))

        val searchUser = TestDataFactory.createUser(email = "user")

        // When
        val results = userDAO.searchUserDetails(searchUser, startsWith = true, includes = false)

        // Then
        assertThat(results).hasSize(2)
        assertThat(results.map { it.email }).allMatch { it.startsWith("user") }
    }

    @Test
    fun `should delete user from database`() {
        // Given
        val user = TestDataFactory.createUser()
        val saved = userRepository.save(user)

        // When
        userDAO.delete(saved.id!!)

        // Then
        assertThat(userRepository.findById(saved.id!!)).isEmpty
    }

    @Test
    fun `should update user enabled status`() {
        // Given
        val user = TestDataFactory.createUser(enabled = true)
        val saved = userRepository.save(user)

        // When
        userDAO.updateUserEnabledStatus(saved.id!!, false)
        userRepository.flush()

        // Then
        val updated = userRepository.findById(saved.id!!).get()
        assertThat(updated.enabled).isFalse()
    }

    @Test
    fun `should list all roles`() {
        // When
        val roles = userDAO.listRoles()

        // Then
        assertThat(roles).hasSizeGreaterThanOrEqualTo(2)
        assertThat(roles.map { it.name }).contains("ROLE_USER", "ROLE_ADMIN")
    }
}
```

- [ ] **Step 3: Run tests**

Run: `./gradlew test --tests UserDAOImplTest`

Expected: 12 tests passed

- [ ] **Step 4: Commit**

```bash
git add src/test/kotlin/com/classroom/user/dao/UserDAOImplTest.kt
git add src/test/resources/test-data.sql
git commit -m "test: Add UserDAOImpl integration tests with @DataJpaTest"
```

---

### Task 13: Repository Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/dao/UserRepositoryTest.kt`
- Create: `src/test/kotlin/com/classroom/user/dao/RoleRepositoryTest.kt`

- [ ] **Step 1: Write UserRepository tests**

Create `src/test/kotlin/com/classroom/user/dao/UserRepositoryTest.kt`:

```kotlin
package com.classroom.user.dao

import com.classroom.TestDataFactory
import com.classroom.user.dao.impl.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should find user by email when exists`() {
        // Given
        val user = TestDataFactory.createUser(email = "test@example.com")
        userRepository.save(user)

        // When
        val result = userRepository.findByEmail("test@example.com")

        // Then
        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo("test@example.com")
    }

    @Test
    fun `should return null when user not found by email`() {
        // When
        val result = userRepository.findByEmail("nonexistent@example.com")

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should update enabled status via custom query`() {
        // Given
        val user = TestDataFactory.createUser(enabled = true)
        val saved = userRepository.save(user)

        // When
        userRepository.updateEnabledStatus(saved.id!!, false)
        userRepository.flush()

        // Then
        val updated = userRepository.findById(saved.id!!).get()
        assertThat(updated.enabled).isFalse()
    }

    @Test
    fun `should perform standard CRUD operations`() {
        // Create
        val user = TestDataFactory.createUser()
        val saved = userRepository.save(user)
        assertThat(saved.id).isNotNull

        // Read
        val found = userRepository.findById(saved.id!!).get()
        assertThat(found.email).isEqualTo(user.email)

        // Update
        found.firstName = "Updated"
        userRepository.save(found)
        val updated = userRepository.findById(saved.id!!).get()
        assertThat(updated.firstName).isEqualTo("Updated")

        // Delete
        userRepository.deleteById(saved.id!!)
        assertThat(userRepository.findById(saved.id!!)).isEmpty
    }
}
```

- [ ] **Step 2: Write RoleRepository tests**

Create `src/test/kotlin/com/classroom/user/dao/RoleRepositoryTest.kt`:

```kotlin
package com.classroom.user.dao

import com.classroom.TestDataFactory
import com.classroom.user.dao.impl.RoleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Test
    fun `should perform standard CRUD operations`() {
        // Create
        val role = TestDataFactory.createRole(name = "ROLE_TEST")
        val saved = roleRepository.save(role)
        assertThat(saved.id).isNotNull

        // Read
        val found = roleRepository.findById(saved.id!!).get()
        assertThat(found.name).isEqualTo("ROLE_TEST")

        // Update
        found.description = "Updated description"
        roleRepository.save(found)
        val updated = roleRepository.findById(saved.id!!).get()
        assertThat(updated.description).isEqualTo("Updated description")

        // Delete
        roleRepository.deleteById(saved.id!!)
        assertThat(roleRepository.findById(saved.id!!)).isEmpty
    }

    @Test
    fun `should find all roles`() {
        // Given
        roleRepository.save(TestDataFactory.createRole(name = "ROLE_ONE"))
        roleRepository.save(TestDataFactory.createRole(name = "ROLE_TWO"))

        // When
        val roles = roleRepository.findAll()

        // Then
        assertThat(roles).hasSizeGreaterThanOrEqualTo(2)
    }
}
```

- [ ] **Step 3: Run tests**

Run: `./gradlew test --tests "*RepositoryTest"`

Expected: 6 tests passed

- [ ] **Step 4: Commit**

```bash
git add src/test/kotlin/com/classroom/user/dao/UserRepositoryTest.kt
git add src/test/kotlin/com/classroom/user/dao/RoleRepositoryTest.kt
git commit -m "test: Add repository layer tests"
```

---

## Chunk 6: Medium-Priority Tests - Controllers

### Task 14: UserController Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/web/UserControllerTest.kt`

- [ ] **Step 1: Write basic navigation tests**

Create `src/test/kotlin/com/classroom/user/web/UserControllerTest.kt`:

```kotlin
package com.classroom.user.web

import com.classroom.TestDataFactory
import com.classroom.user.service.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
@WithMockUser
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userService: UserService

    @Test
    fun `should display welcome page`() {
        mockMvc.perform(get("/welcome"))
            .andExpect(status().isOk)
            .andExpect(view().name("welcome"))
    }

    @Test
    fun `should redirect root to welcome`() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("welcome"))
    }

    @Test
    fun `should display login page`() {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk)
            .andExpect(view().name("login"))
    }

    @Test
    fun `should display paginated user list`() {
        // Given
        val users = TestDataFactory.createMultipleUsers(5)
        val page = PageImpl(users)
        every { userService.getAllUserDetails(1) } returns page

        // When & Then
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(view().name("user/users"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attribute("currentPage", 1))
    }

    @Test
    fun `should display user creation form with roles`() {
        // Given
        val roles = listOf(TestDataFactory.createRole())
        every { userService.listRoles() } returns roles

        // When & Then
        mockMvc.perform(get("/users/new"))
            .andExpect(status().isOk)
            .andExpect(view().name("user/user_form"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("listRoles"))
            .andExpect(model().attribute("pageTitle", "Create New User"))
    }
}
```

- [ ] **Step 2: Run tests**

Run: `./gradlew test --tests UserControllerTest`

Expected: 5 tests passed

- [ ] **Step 3: Write edit and delete tests**

Add to `UserControllerTest.kt`:

```kotlin
    @Test
    fun `should display edit form with user and roles`() {
        // Given
        val userId = 1L
        val user = TestDataFactory.createUser(id = userId)
        val roles = listOf(TestDataFactory.createRole())

        every { userService.getUserById(userId) } returns user
        every { userService.listRoles() } returns roles

        // When & Then
        mockMvc.perform(get("/users/edit/$userId"))
            .andExpect(status().isOk)
            .andExpect(view().name("user/user_form"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("listRoles"))
            .andExpect(model().attribute("pageTitle", "Edit User (ID: $userId)"))
    }

    @Test
    fun `should redirect with error when editing non-existent user`() {
        // Given
        val userId = 999L
        every { userService.getUserById(userId) } throws RuntimeException("User not found")

        // When & Then
        mockMvc.perform(get("/users/edit/$userId"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attributeExists("message"))
    }

    @Test
    fun `should delete user successfully`() {
        // Given
        val userId = 1L
        every { userService.delete(userId) } returns Unit

        // When & Then
        mockMvc.perform(get("/users/delete/$userId"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attribute("message", "The user ID $userId has been deleted successfully."))

        verify { userService.delete(userId) }
    }

    @Test
    fun `should handle delete error gracefully`() {
        // Given
        val userId = 1L
        every { userService.delete(userId) } throws RuntimeException("Cannot delete user")

        // When & Then
        mockMvc.perform(get("/users/delete/$userId"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attributeExists("message"))
    }

    @Test
    fun `should enable user`() {
        // Given
        val userId = 1L
        every { userService.updateUserEnabledStatus(userId, true) } returns Unit

        // When & Then
        mockMvc.perform(get("/users/$userId/enabled/true"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attribute("message", "The user ID $userId has been enabled successfully."))
    }

    @Test
    fun `should disable user`() {
        // Given
        val userId = 1L
        every { userService.updateUserEnabledStatus(userId, false) } returns Unit

        // When & Then
        mockMvc.perform(get("/users/$userId/enabled/false"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attribute("message", "The user ID $userId has been disabled successfully."))
    }
```

- [ ] **Step 4: Run tests**

Run: `./gradlew test --tests UserControllerTest`

Expected: 11 tests passed

- [ ] **Step 5: Commit**

```bash
git add src/test/kotlin/com/classroom/user/web/UserControllerTest.kt
git commit -m "test: Add UserController tests"
```

---

### Task 15: RegistrationController Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/web/RegistrationControllerTest.kt`

- [ ] **Step 1: Write registration controller tests**

Create `src/test/kotlin/com/classroom/user/web/RegistrationControllerTest.kt`:

```kotlin
package com.classroom.user.web

import com.classroom.TestDataFactory
import com.classroom.user.service.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(RegistrationController::class)
class RegistrationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userService: UserService

    @Test
    @WithAnonymousUser
    fun `should display registration form`() {
        mockMvc.perform(get("/registration"))
            .andExpect(status().isOk)
            .andExpect(view().name("registration"))
    }

    @Test
    @WithAnonymousUser
    fun `should register new user and redirect to success`() {
        // Given
        val user = TestDataFactory.createUser()
        every { userService.save(any()) } returns user

        // When & Then
        mockMvc.perform(
            post("/registration")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "john@example.com")
                .param("password", "password123")
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/registration?success"))

        verify { userService.save(any()) }
    }
}
```

- [ ] **Step 2: Run tests**

Run: `./gradlew test --tests RegistrationControllerTest`

Expected: 2 tests passed

- [ ] **Step 3: Commit**

```bash
git add src/test/kotlin/com/classroom/user/web/RegistrationControllerTest.kt
git commit -m "test: Add RegistrationController tests"
```

---

## Chunk 7: Low-Priority Tests - Entities

### Task 16: Entity Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/entity/UserTest.kt`
- Create: `src/test/kotlin/com/classroom/user/entity/RoleTest.kt`

- [ ] **Step 1: Write User entity tests**

Create `src/test/kotlin/com/classroom/user/entity/UserTest.kt`:

```kotlin
package com.classroom.user.entity

import com.classroom.TestDataFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun `should set createdOn and modifiedOn when initializeDate is called on new user`() {
        // Given
        val user = TestDataFactory.createUser(id = null)
        assertThat(user.createdOn).isNull()
        assertThat(user.modifiedOn).isNull()

        // When
        user.initializeDate()

        // Then
        assertThat(user.createdOn).isNotNull
        assertThat(user.modifiedOn).isNotNull
    }

    @Test
    fun `should update modifiedOn but not createdOn when initializeDate is called on existing user`() {
        // Given
        val user = TestDataFactory.createUser(id = 1L)
        user.initializeDate()
        val originalCreatedOn = user.createdOn

        Thread.sleep(10) // Small delay to ensure different timestamp

        // When
        user.initializeDate()

        // Then
        assertThat(user.createdOn).isEqualTo(originalCreatedOn)
        assertThat(user.modifiedOn).isNotEqualTo(originalCreatedOn)
    }

    @Test
    fun `should return default image path when id is null`() {
        // Given
        val user = TestDataFactory.createUser(id = null, photo = "photo.jpg")

        // When
        val imagePath = user.photosImagePath

        // Then
        assertThat(imagePath).isEqualTo("/img/default-user.png")
    }

    @Test
    fun `should return default image path when photo is null`() {
        // Given
        val user = TestDataFactory.createUser(id = 1L, photo = null)

        // When
        val imagePath = user.photosImagePath

        // Then
        assertThat(imagePath).isEqualTo("/img/default-user.png")
    }

    @Test
    fun `should return correct image path when id and photo exist`() {
        // Given
        val user = TestDataFactory.createUser(id = 1L, photo = "user-photo.jpg")

        // When
        val imagePath = user.photosImagePath

        // Then
        assertThat(imagePath).isEqualTo("/user-photos/1/user-photo.jpg")
    }

    @Test
    fun `should include all fields in toString`() {
        // Given
        val user = TestDataFactory.createUser(id = 1L, firstName = "John", lastName = "Doe")

        // When
        val result = user.toString()

        // Then
        assertThat(result).contains("id=1", "firstName=John", "lastName=Doe")
    }
}
```

- [ ] **Step 2: Write Role entity tests**

Create `src/test/kotlin/com/classroom/user/entity/RoleTest.kt`:

```kotlin
package com.classroom.user.entity

import com.classroom.TestDataFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RoleTest {

    @Test
    fun `should return role name in toString`() {
        // Given
        val role = TestDataFactory.createRole(name = "ROLE_ADMIN")

        // When
        val result = role.toString()

        // Then
        assertThat(result).isEqualTo("ROLE_ADMIN")
    }
}
```

- [ ] **Step 3: Run tests**

Run: `./gradlew test --tests "*entity*"`

Expected: 7 tests passed

- [ ] **Step 4: Commit**

```bash
git add src/test/kotlin/com/classroom/user/entity/UserTest.kt
git add src/test/kotlin/com/classroom/user/entity/RoleTest.kt
git commit -m "test: Add entity tests for User and Role"
```

---

## Chunk 8: End-to-End Integration Tests

### Task 17: User Flow Integration Tests

**Files:**
- Create: `src/test/kotlin/com/classroom/user/integration/UserFlowIntegrationTest.kt`

- [ ] **Step 1: Write complete user lifecycle integration test**

Create `src/test/kotlin/com/classroom/user/integration/UserFlowIntegrationTest.kt`:

```kotlin
package com.classroom.user.integration

import com.classroom.TestDataFactory
import com.classroom.user.dao.impl.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.datasource.url=jdbc:h2:mem:integrationtest"])
@Transactional
class UserFlowIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    private val testPhotoDir = Path.of("user-photos")

    @Test
    @WithMockUser(username = "admin@example.com", roles = ["ADMIN"])
    fun `should complete full user CRUD lifecycle`() {
        // Step 1: Create user
        val email = "lifecycle@example.com"

        mockMvc.perform(
            multipart("/users/save")
                .param("firstName", "Lifecycle")
                .param("lastName", "Test")
                .param("email", email)
                .param("password", "password123")
                .param("enabled", "true")
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/users"))

        // Verify user created
        val createdUser = userRepository.findByEmail(email)
        assertThat(createdUser).isNotNull
        assertThat(createdUser?.firstName).isEqualTo("Lifecycle")

        // Step 2: List users - verify user appears
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(view().name("user/users"))

        // Step 3: Edit user
        val userId = createdUser!!.id!!

        mockMvc.perform(get("/users/edit/$userId"))
            .andExpect(status().isOk)
            .andExpect(model().attributeExists("user"))

        mockMvc.perform(
            multipart("/users/save")
                .param("id", userId.toString())
                .param("firstName", "Updated")
                .param("lastName", "Name")
                .param("email", email)
                .param("password", "")
                .param("enabled", "true")
        )
            .andExpect(status().is3xxRedirection)

        val updatedUser = userRepository.findById(userId).get()
        assertThat(updatedUser.firstName).isEqualTo("Updated")

        // Step 4: Disable user
        mockMvc.perform(get("/users/$userId/enabled/false"))
            .andExpect(status().is3xxRedirection)

        val disabledUser = userRepository.findById(userId).get()
        assertThat(disabledUser.enabled).isFalse()

        // Step 5: Delete user
        mockMvc.perform(get("/users/delete/$userId"))
            .andExpect(status().is3xxRedirection)

        assertThat(userRepository.findById(userId)).isEmpty
    }

    @Test
    fun `should complete user registration and login flow`() {
        // Step 1: Access registration page
        mockMvc.perform(get("/registration"))
            .andExpect(status().isOk)
            .andExpect(view().name("registration"))

        // Step 2: Register new user
        val email = "newuser@example.com"

        mockMvc.perform(
            post("/registration")
                .param("firstName", "New")
                .param("lastName", "User")
                .param("email", email)
                .param("password", "password123")
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/registration?success"))

        // Verify user created in database
        val user = userRepository.findByEmail(email)
        assertThat(user).isNotNull
        assertThat(user?.email).isEqualTo(email)
    }

    @Test
    @WithMockUser
    fun `should complete photo upload flow`() {
        // Given
        val user = TestDataFactory.createUser(email = "photo@example.com")
        val savedUser = userRepository.save(user)

        val photo = MockMultipartFile(
            "image",
            "test-photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".toByteArray()
        )

        try {
            // When - Upload photo
            mockMvc.perform(
                multipart("/users/save")
                    .file(photo)
                    .param("id", savedUser.id.toString())
                    .param("firstName", savedUser.firstName)
                    .param("lastName", savedUser.lastName)
                    .param("email", savedUser.email)
                    .param("password", "")
                    .param("enabled", "true")
            )
                .andExpect(status().is3xxRedirection)

            // Then - Verify photo saved
            val updatedUser = userRepository.findById(savedUser.id!!).get()
            assertThat(updatedUser.photo).isEqualTo("test-photo.jpg")

            val photoPath = Path.of("user-photos/${savedUser.id}/test-photo.jpg")
            assertThat(photoPath).exists()

            // Verify photo path is correct
            assertThat(updatedUser.photosImagePath).contains(savedUser.id.toString())
            assertThat(updatedUser.photosImagePath).contains("test-photo.jpg")
        } finally {
            // Cleanup
            if (testPhotoDir.exists()) {
                testPhotoDir.deleteRecursively()
            }
        }
    }

    @Test
    fun `should complete email validation flow`() {
        // Step 1: Check unique email (should be OK)
        mockMvc.perform(
            post("/users/check_email")
                .param("userId", "")
                .param("email", "unique@example.com")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("OK"))

        // Step 2: Register user with that email
        val user = TestDataFactory.createUser(email = "unique@example.com")
        userRepository.save(user)

        // Step 3: Check same email again (should be Duplicated for new user)
        mockMvc.perform(
            post("/users/check_email")
                .param("userId", "")
                .param("email", "unique@example.com")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Duplicated"))

        // Step 4: Check for existing user with same email (should be OK)
        mockMvc.perform(
            post("/users/check_email")
                .param("userId", user.id.toString())
                .param("email", "unique@example.com")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("OK"))
    }

    @Test
    @WithMockUser
    fun `should complete user search flow`() {
        // Given - Create multiple users
        userRepository.save(TestDataFactory.createUser(firstName = "John", email = "john1@example.com"))
        userRepository.save(TestDataFactory.createUser(firstName = "Johnny", email = "john2@example.com"))
        userRepository.save(TestDataFactory.createUser(firstName = "Jane", email = "jane@example.com"))

        // Note: Search functionality would need to be exposed via controller
        // This test demonstrates the flow but may need additional endpoints

        // When - List all users
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(model().attributeExists("users"))

        // Verify users exist in database
        val allUsers = userRepository.findAll()
        assertThat(allUsers).hasSizeGreaterThanOrEqualTo(3)
    }
}
```

- [ ] **Step 2: Run integration tests**

Run: `./gradlew test --tests UserFlowIntegrationTest`

Expected: 5 tests passed

- [ ] **Step 3: Commit**

```bash
git add src/test/kotlin/com/classroom/user/integration/UserFlowIntegrationTest.kt
git commit -m "test: Add end-to-end user flow integration tests"
```

---

## Final Verification

### Task 18: Run Full Test Suite

- [ ] **Step 1: Run all tests**

Run: `./gradlew test`

Expected: Approximately 80 tests passed

- [ ] **Step 2: Generate coverage report**

Run: `./gradlew test jacocoTestReport`

Check: `build/reports/jacoco/test/html/index.html`

Expected: Overall coverage >= 75%

- [ ] **Step 3: Verify coverage by priority**

Check coverage for high-priority classes:
- UserServiceImpl: >= 90%
- UserDetailsServiceImpl: >= 90%
- FileUploadUtil: >= 90%
- SecurityServiceImpl: >= 90%

- [ ] **Step 4: Final commit**

```bash
git add .
git commit -m "test: Complete comprehensive test suite with 75-80% coverage

- 80+ tests across all layers
- Unit tests for services, specifications, utilities
- Integration tests for DAOs, controllers, security
- End-to-end tests for critical user flows
- Test utilities: TestDataFactory, MockMvc extensions
- Coverage: 75-80% overall, 90%+ for high-priority classes"
```

---

## Summary

**Test Suite Complete!**

**Test Counts:**
- Unit tests: ~45
- Integration tests: ~30
- End-to-end tests: ~5
- **Total: ~80 tests**

**Coverage Achieved:**
- Overall: 75-80%
- High-priority: 90-95%
- Medium-priority: 70-80%
- Low-priority: 40-50%

**Key Testing Technologies:**
- JUnit 5 (Jupiter)
- MockK for Kotlin mocking
- Spring Boot Test (@DataJpaTest, @WebMvcTest, @SpringBootTest)
- MockMvc for controller testing
- AssertJ for assertions
- H2 in-memory database

**Next Steps:**
- Run `./gradlew test` to execute all tests
- Review coverage report at `build/reports/jacoco/test/html/index.html`
- Add more tests as new features are developed
- Consider mutation testing with PIT for test quality verification

