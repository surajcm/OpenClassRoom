# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

OpenClassRoom is an online classroom platform built with Spring Boot 3.4 and Kotlin 2.1, allowing users to register as students or teachers. The application uses a traditional MVC architecture with Thymeleaf templates for server-side rendering.

## Build & Development Commands

### Prerequisites
- Java 21 (JDK 21 required)
- Gradle 8.11.1 (via wrapper)

### Common Commands

**Build the project:**
```bash
./gradlew build
```

**Run tests:**
```bash
./gradlew test
```

**Run the application:**
```bash
./gradlew bootRun
```
Application runs on port 8181 (configured in application.properties).

**Run with custom build script (sets JAVA_HOME and memory options):**
```bash
./build.sh bootRun
```

**Code quality checks:**
```bash
./gradlew checkstyleMain checkstyleTest  # Code style
./gradlew pmdMain pmdTest                # PMD analysis
./gradlew jacocoTestReport               # Code coverage report
```

**Clean build:**
```bash
./gradlew clean build
```

## Architecture

### Layered Architecture

The application follows a strict layered architecture pattern:

```
web (Controllers) → service → dao (Data Access) → entities (JPA)
```

**Package Structure:**
- `com.classroom.init` - Configuration, security, utilities, and shared specifications
- `com.classroom.user` - User domain with complete layered structure
  - `web/` - Controllers (both @Controller for MVC and @RestController for API)
  - `service/` - Business logic interfaces and implementations
  - `dao/` - Data access interfaces and implementations
  - `dao/impl/entities/` - JPA entities (User, Role)
  - `dao/spec/` - JPA Specification implementations for dynamic queries
  - `exception/` - Domain-specific exceptions
- `com.classroom.misc` - Miscellaneous controllers

### Key Design Patterns

**DAO Pattern:**
- Each domain has a DAO interface (e.g., `UserDAO`) and implementation (`UserDAOImpl`)
- DAOs delegate to Spring Data JPA repositories for basic CRUD
- Complex queries use JPA Specifications

**JPA Specification Pattern for Dynamic Queries:**
The application uses a custom Specification framework for building dynamic search queries:
- `SearchCriteria` - Encapsulates search parameters (key, value, operation)
- `SearchOperation` - Enum defining query operations (EQUAL, MATCH, MATCH_START)
- `UserSpecification` - Builds JPA predicates from multiple SearchCriteria
- Example usage in `UserDAOImpl.searchAllUsers()`

**Service Layer:**
- Services contain business logic and transaction management
- Each service has an interface and implementation (e.g., `UserService`, `UserServiceImpl`)
- `SecurityService` handles authentication operations

### Database

**H2 In-Memory Database:**
- Schema defined in `src/main/resources/schema.sql`
- Initial data in `src/main/resources/data.sql`
- Schema validation mode: `spring.jpa.hibernate.ddl-auto=validate`
- Physical naming strategy preserves exact column names from entities

**Entity Lifecycle:**
- Entities use `@PrePersist` and `@PreUpdate` for automatic timestamp management
- `User` entity includes `createdOn`, `modifiedOn`, `createdBy`, `modifiedBy` fields

### Security

**Spring Security Configuration (WebSecurityConfig.kt):**
- BCrypt password encoding
- Form-based authentication with custom login page (`/login`)
- Method-level security enabled (`@EnableMethodSecurity`)
- Excluded paths: `/resources/**`, `/css/**`, `/js/**`, `/img/**`, `/user-photos/**`, `/assets/**`, `/registration`

**User Roles:**
- Many-to-many relationship between User and Role entities
- Join table: `users_roles`

### File Upload Handling

**FileUploadUtil:**
- Handles user photo uploads
- Upload directory: `user-photos/{userId}/`
- Cleans directory before saving new file
- Default user image: `/img/default-user.png`
- Photo path computed in `User.photosImagePath()` transient method

### Pagination

**User Listing:**
- Configurable page size in `Constants.USERS_PER_PAGE` (currently 5)
- Uses Spring Data's `Page` and `PageRequest`
- URLs follow pattern: `/user/page/{pageNumber}`

### REST API Endpoints

**User REST Controller:**
- `POST /users/check_email` - Validates email uniqueness (returns "OK" or "Duplicated")

### Templates

**Thymeleaf Templates:**
- Located in `src/main/resources/templates/`
- Main layout includes `navbar.html`
- User-specific templates in `templates/user/`
- Template security: Thymeleaf Spring Security integration enabled

## Development Guidelines

### Adding New Entities

1. Create entity class in appropriate `dao/impl/entities/` package
2. Add repository interface extending `JpaRepository`
3. Create DAO interface and implementation
4. If dynamic queries needed, create Specification class
5. Create service interface and implementation
6. Add controllers in `web/` package

### Static Code Analysis

The project enforces code quality through:
- **Checkstyle** (v10.20.2) - Separate configs for main and test code
  - Main: `config/checkstyle/checkstyle.xml`
  - Test: `config/checkstyle/checkstyleTest.xml`
- **PMD** (v7.8.0) - Rules in `config/pmd/ruleset.xml`
- **JaCoCo** (v0.8.12) - Code coverage reports in `build/reports/jacoco/`

All checks must pass (`ignoreFailures = false`).

### Kotlin Code Standards

This codebase uses **idiomatic Kotlin** throughout. Follow these standards rigorously:

**Entity Classes:**
- Use primary constructors with default values
- Non-nullable types for required database fields (`var name: String = ""`)
- Nullable types only for truly optional fields (`var photo: String? = null`)
- Use `mutableSetOf()` for collections, not `HashSet()`
- Use property syntax with custom getters instead of methods
- Expression body functions where appropriate

```kotlin
// Good
class User(
    var id: Long? = null,
    var firstName: String = "",
    var roles: MutableSet<Role> = mutableSetOf()
) {
    @get:Transient
    val photosImagePath: String
        get() = when {
            id == null || photo == null -> "/img/default-user.png"
            else -> "/user-photos/$id/$photo"
        }
}
```

**Repository Interfaces:**
- Use `JpaRepository<Entity, ID>` (includes CRUD and paging)
- No need for `@Repository` annotation on interfaces
- Add `JpaSpecificationExecutor` for dynamic queries

```kotlin
interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User>
```

**Service Layer:**
- Use constructor injection (private val/var)
- Expression body functions for single expressions
- `when` expressions instead of if-else chains
- No `@Throws` annotations (Kotlin doesn't need them)
- Use scope functions: `apply`, `let`, `also`, `takeIf`, `run`

```kotlin
override fun save(user: User): User {
    user.password = when {
        user.id != null -> {
            val existing = userDAO.findById(user.id!!)
            when {
                user.password.isNotBlank() -> passwordEncoder.encode(user.password)
                else -> existing?.password ?: ""
            }
        }
        else -> passwordEncoder.encode(user.password)
    }
    return userDAO.save(user)
}
```

**DAO Layer:**
- Use `findByIdOrNull()` extension instead of `Optional.orElseThrow()`
- Expression body functions
- Use `takeIf`, `let` for conditional operations
- String templates: `"user-photos/$id"` not `"user-photos/" + id`

**Controllers:**
- Private dependencies and loggers
- Use `apply` for setting multiple model attributes
- Use `runCatching` for exception handling
- String templates in paths and messages
- Null safety: `isNullOrBlank()` instead of `== null || isEmpty()`

**Utilities:**
- Use `object` for singletons (not instantiable classes)
- Kotlin Path API: `kotlin.io.path.Path`, `absolutePathString()`
- Extension functions: `exists()`, `isDirectory()`, `deleteIfExists()`
- `use` for auto-closing resources
- `runCatching` for functional error handling

**Never Do:**
- ❌ Force unwrap with `!!` (except after explicit null check)
- ❌ Unnecessary nullable types (`User?` when User is never null)
- ❌ `@Throws` annotations
- ❌ Java collections: `HashSet()`, `ArrayList()` - use `mutableSetOf()`, `mutableListOf()`
- ❌ `open` classes (all-open plugin handles JPA entities)
- ❌ Multiple repository inheritance when `JpaRepository` suffices
- ❌ `.or()`, `.and()` - use `||`, `&&`
- ❌ Verbose try-catch that just rethrows
- ❌ `return` in single-expression functions

**Always Do:**
- ✅ Primary constructors for entity classes
- ✅ Expression body functions (no curly braces for single expression)
- ✅ `when` expressions for conditional logic
- ✅ String templates for string building
- ✅ Scope functions for context operations
- ✅ Kotlin collection functions: `map`, `filter`, `forEach`
- ✅ Elvis operator `?:` for null coalescing
- ✅ Safe call `?.` for nullable access
- ✅ Smart casts with `is`

## Logging

- Log4j2 used (Spring Boot default logging excluded)
- Controllers use Apache Commons Logging
- DAO implementations use SLF4J

## Known Issues / TODOs

- SpotBugs plugin is commented out in staticCodeAnalysis.gradle
- SonarQube analysis is commented out in CI workflow

## Recent Changes

The codebase was recently converted from Java-style Kotlin to idiomatic Kotlin. See `KOTLIN_CONVERSION_SUMMARY.md` for details.
