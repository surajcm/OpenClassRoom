# Test Suite Design - OpenClassRoom

**Date:** 2026-03-15
**Project:** OpenClassRoom - Online Classroom Platform
**Objective:** Create a comprehensive, risk-based test suite for the recently kotlinized codebase

## Goals

Create a balanced test suite that provides:
1. **Confidence in refactoring** - Safe evolution of the codebase
2. **Living documentation** - Tests that show how the system works
3. **Critical path protection** - Focus on high-impact user flows
4. **Fast feedback** - Mix of unit and integration tests

## Testing Strategy

### Approach: Risk-Based Testing

Focus testing effort where bugs would hurt most, using a balanced mix of unit and integration tests.

**Priority Levels:**

- **High Priority (90-95% coverage):** Authentication, security, user CRUD, email validation, file uploads
- **Medium Priority (70-80% coverage):** Search specifications, repositories, controllers
- **Low Priority (40-50% coverage):** Entities, configuration classes

**Expected Overall Coverage:** 75-80%

### Test Architecture

```
src/test/kotlin/com/classroom/
├── user/
│   ├── service/           (unit tests - mocked dependencies)
│   ├── dao/               (integration tests - @DataJpaTest)
│   ├── web/               (controller tests - @WebMvcTest)
│   └── integration/       (end-to-end tests - @SpringBootTest)
├── init/                  (utilities and config tests)
└── TestDataFactory.kt     (shared test data builders)
```

### Test Types

**Unit Tests** - Fast, isolated, mocked dependencies
- Services (mock DAOs)
- Specifications (mock JPA criteria)
- Utilities (FileUploadUtil)

**Integration Tests** - Spring context, real components
- DAOs with `@DataJpaTest` (H2 database)
- Controllers with `@WebMvcTest` (mock services)
- Security with `@SpringBootTest`

**End-to-End Tests** - Full stack
- Critical user flows with `@SpringBootTest`
- Real HTTP requests via `TestRestTemplate`

## Detailed Test Coverage

### High Priority Tests

#### 1. Authentication & Security

**UserDetailsServiceImplTest.kt** (Unit)
- Load user by email - success case
- Load user by email - throws UsernameNotFoundException when not found
- Map roles to granted authorities correctly
- Return correct username, password, authorities

**SecurityServiceImplTest.kt** (Unit)
- Find logged-in username - authenticated user
- Find logged-in username - no authentication returns null
- isAuthenticated - valid authentication returns true
- isAuthenticated - anonymous token returns false
- isAuthenticated - null authentication returns false

**WebSecurityConfigTest.kt** (Integration - @SpringBootTest)
- Public paths accessible without auth (/login, /registration, /css/**, /img/**)
- Protected paths require authentication (/users, /welcome)
- Password encoder bean is BCrypt
- Form login redirects to login page
- Default success URL after login

#### 2. User Service (Business Logic)

**UserServiceImplTest.kt** (Unit - Mock UserDAO, PasswordEncoder)
- save() new user - password gets encoded
- save() existing user with new password - password gets encoded
- save() existing user with empty password - keeps existing password
- save() existing user - fetches existing password correctly
- isEmailUnique() new user, email exists - returns false
- isEmailUnique() new user, email doesn't exist - returns true
- isEmailUnique() editing user, same email - returns true
- isEmailUnique() editing user, different email exists - returns false
- getAllUserDetails() returns paginated results with correct page
- searchUserDetails() delegates to DAO with correct parameters
- getUserById() returns user from DAO
- delete() delegates to DAO
- updateUserEnabledStatus() delegates to DAO
- listRoles() returns roles from DAO

#### 3. File Upload

**FileUploadUtilTest.kt** (Unit - File system operations)
- saveFile() creates directory if not exists
- saveFile() saves file successfully to correct path
- saveFile() throws IOException on failure with descriptive message
- saveFile() handles multipart file correctly
- cleanDir() deletes only files, not directories
- cleanDir() handles non-existent directory gracefully
- cleanDir() handles IOException gracefully (prints message)
- cleanDir() doesn't fail when directory is empty

#### 4. Email Validation API

**UserRestControllerTest.kt** (@WebMvcTest - Mock UserService)
- POST /users/check_email - new user (null id), unique email returns "OK"
- POST /users/check_email - new user (null id), duplicate email returns "Duplicated"
- POST /users/check_email - existing user, same email returns "OK"
- POST /users/check_email - existing user, different duplicate returns "Duplicated"
- Correct content type (text/plain)

### Medium Priority Tests

#### 5. Search Specifications

**UserSpecificationTest.kt** (Unit - Mock CriteriaBuilder, Root)
- toPredicate() with EQUAL operation creates equal predicate
- toPredicate() with MATCH operation creates case-insensitive like predicate with wildcards
- toPredicate() with MATCH_START operation creates case-insensitive like predicate with trailing wildcard
- Multiple criteria combined with AND logic
- Empty criteria list returns predicate that matches all
- Lowercase conversion works correctly

#### 6. DAO Layer

**UserDAOImplTest.kt** (@DataJpaTest - Real H2 database)
- getAllUserDetails() returns correct page of users
- getAllUserDetails() calculates page offset correctly (pageNumber - 1)
- getAllUserDetails() respects USERS_PER_PAGE constant
- addNewUser() saves new user to database
- updateUser() updates existing user fields (firstName, lastName, email, password, roles, enabled, modifiedBy)
- updateUser() does nothing when user id is null
- findByEmail() finds user by email
- findByEmail() returns null when not found
- findById() returns user when exists
- findById() throws UserNotFoundException when not exists
- searchUserDetails() searches by firstName with MATCH operation
- searchUserDetails() searches by email with MATCH_START operation
- searchUserDetails() searches by roles
- searchUserDetails() combines multiple criteria
- searchUserDetails() skips blank firstName
- searchUserDetails() skips blank email
- searchUserDetails() skips empty roles
- delete() removes user from database
- updateUserEnabledStatus() updates enabled flag via custom query
- listRoles() returns all roles from repository
- save() persists user and returns saved entity

#### 7. Controller Tests

**UserControllerTest.kt** (@WebMvcTest - Mock UserService)
- GET / redirects to welcome page
- GET /welcome displays welcome page
- GET /login displays login form
- GET /login with error displays error message
- GET /login with logout displays logout message
- GET /users displays paginated user list (page 1)
- GET /user/page/{pageNumber} displays correct page
- GET /user/page/{pageNumber} calculates startCount correctly
- GET /user/page/{pageNumber} calculates endCount correctly
- GET /user/page/{pageNumber} handles last page (endCount = totalElements)
- GET /users/new displays user creation form
- GET /users/new includes all roles in model
- GET /users/new sets enabled to true by default
- POST /users/save saves user without photo
- POST /users/save saves user with photo upload
- POST /users/save cleans directory before saving photo
- POST /users/save handles empty photo field
- GET /users/edit/{id} displays edit form
- GET /users/edit/{id} includes user and roles in model
- GET /users/edit/{id} redirects on user not found with error message
- GET /users/delete/{id} deletes user successfully
- GET /users/delete/{id} handles exception gracefully with error message
- GET /users/{id}/enabled/{status} enables user
- GET /users/{id}/enabled/{status} disables user
- GET /user/preferences displays preferences page
- GET /user/logMeOut clears security context and invalidates session

**RegistrationControllerTest.kt** (@WebMvcTest - Mock UserService)
- GET /registration displays registration form
- POST /registration registers new user
- POST /registration redirects to success page

#### 8. Repository Layer

**UserRepositoryTest.kt** (@DataJpaTest)
- findByEmail() returns user when exists
- findByEmail() returns null when not exists
- updateEnabledStatus() updates enabled flag via JPQL query
- Standard CRUD operations work (save, findById, delete, findAll)
- Pagination works correctly
- JpaSpecificationExecutor methods work (findAll with Specification)

**RoleRepositoryTest.kt** (@DataJpaTest)
- Standard CRUD operations work (save, findById, delete, findAll)

### Low Priority Tests

#### 9. Entity Tests

**UserTest.kt** (Unit)
- initializeDate() sets createdOn when id is null (@PrePersist)
- initializeDate() sets modifiedOn on both persist and update
- initializeDate() doesn't reset createdOn on update
- photosImagePath returns default when id is null
- photosImagePath returns default when photo is null
- photosImagePath returns correct path when id and photo exist
- toString() includes all relevant fields

**RoleTest.kt** (Unit)
- toString() returns role name

#### 10. Configuration Tests

**AppMvcConfigTest.kt** (@SpringBootTest)
- user-photos resource handler is configured
- user-photos path is correct

### End-to-End Integration Tests

**UserFlowIntegrationTest.kt** (@SpringBootTest, webEnvironment = RANDOM_PORT)

Complete user flows testing the entire stack:

1. **User Registration Flow**
   - POST /registration with valid data
   - Verify user saved to database
   - Login with registered credentials
   - Access protected page successfully

2. **User CRUD Lifecycle**
   - Create user via POST /users/save
   - List users - verify user appears
   - Edit user via GET /users/edit/{id} and POST /users/save
   - Disable user via GET /users/{id}/enabled/false
   - Delete user via GET /users/delete/{id}
   - Verify user no longer exists

3. **Photo Upload Flow**
   - Create user with photo via POST /users/save (multipart)
   - Verify file saved to user-photos/{userId}/ directory
   - Verify user.photo field set correctly
   - GET user and check photosImagePath

4. **Email Validation Flow**
   - POST /users/check_email with unique email returns "OK"
   - Save user with that email
   - POST /users/check_email with same email returns "Duplicated"
   - POST /users/check_email for existing user with same email returns "OK"

5. **User Search Flow**
   - Create multiple users with varied data
   - Search by firstName (MATCH operation)
   - Search by email (MATCH_START operation)
   - Verify correct users returned

## Test Utilities

### TestDataFactory.kt

Shared factory for creating test data:

```kotlin
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
    ): User

    fun createRole(
        id: Long? = null,
        name: String = "ROLE_USER",
        description: String = "User role"
    ): Role

    fun createUserWithRole(roleName: String = "ROLE_USER"): User

    fun createMultipleUsers(count: Int, emailPrefix: String = "user"): List<User>

    fun createAdminUser(): User
}
```

### MockMvc Extensions (Kotlin DSL)

```kotlin
// Extension functions for cleaner test code
fun MockMvc.getAndExpectOk(url: String): ResultActions

fun MockMvc.postForm(url: String, params: Map<String, String>): ResultActions

fun MockMvc.postMultipart(url: String, file: MockMultipartFile, params: Map<String, String>): ResultActions
```

### Test Configuration

```kotlin
@TestConfiguration
class TestSecurityConfig {
    // Mock security context for easier testing where needed
}
```

## Testing Technologies

- **JUnit 5 (Jupiter)** - Test framework
- **MockK** - Kotlin-friendly mocking library (preferred over Mockito)
- **Spring Boot Test** - Integration testing support
- **MockMvc** - Controller testing
- **AssertJ** or **Kotest assertions** - Fluent assertions
- **H2** - In-memory database for @DataJpaTest

## Coverage Goals

| Layer | Target Coverage | Priority |
|-------|----------------|----------|
| Services | 90-95% | High |
| Security | 90-95% | High |
| File Utilities | 90-95% | High |
| DAOs | 75-80% | Medium |
| Controllers | 75-80% | Medium |
| Specifications | 75-80% | Medium |
| Repositories | 70-75% | Medium |
| Entities | 40-50% | Low |
| Configuration | 40-50% | Low |
| **Overall** | **75-80%** | - |

## Test Counts (Estimated)

- Unit tests: ~45 tests
- Integration tests (@DataJpaTest, @WebMvcTest): ~30 tests
- End-to-end tests (@SpringBootTest): ~5 tests
- **Total: ~80 tests**

## Success Criteria

1. ✅ All tests pass on clean build
2. ✅ Overall code coverage >= 75%
3. ✅ High-priority classes >= 90% coverage
4. ✅ Test suite completes in < 60 seconds
5. ✅ No flaky tests (consistent pass/fail)
6. ✅ Tests serve as documentation
7. ✅ Easy to add new tests following patterns

## Implementation Notes

### Test Isolation

- Each test method is independent
- Use `@BeforeEach` for common setup
- Clean database state between @DataJpaTest tests
- Mock external dependencies (filesystem, time, etc.)

### Kotlin Idioms in Tests

- Use data classes for test DTOs
- Use `apply`, `let`, `also` for test setup
- Extension functions for common patterns
- Named parameters for clarity
- Backtick test names: \`should do something when condition\`

### Naming Conventions

**Test Classes:** `{ClassName}Test.kt`
**Test Methods:** Descriptive names or backticked phrases

```kotlin
@Test
fun `should encode password when saving new user`()

// or

@Test
fun shouldEncodePasswordWhenSavingNewUser()
```

## Dependencies to Add

If not already present, add to `gradle/dependencies.gradle`:

```kotlin
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("com.ninja-squad:springmockk:4.0.2")
testImplementation("org.assertj:assertj-core:3.24.2")
```

## Risks & Mitigations

| Risk | Mitigation |
|------|------------|
| Tests too slow | Use unit tests heavily; limit @SpringBootTest |
| Flaky file system tests | Use temporary directories; clean up in @AfterEach |
| Database state pollution | @DirtiesContext or explicit cleanup |
| Time-dependent tests | Mock clock/time providers |
| Complex JPA mocking | Use @DataJpaTest with real H2 instead |

## Future Enhancements

After initial suite is complete, consider:

1. **Property-based testing** - Use Kotest for edge cases
2. **Mutation testing** - PIT to verify test quality
3. **Performance tests** - Load testing critical endpoints
4. **Contract tests** - API contract verification
5. **Archunit** - Architecture compliance tests

## References

- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [MockK Documentation](https://mockk.io/)
- [Kotlin Test Best Practices](https://kotlinlang.org/docs/jvm-test-using-junit.html)
