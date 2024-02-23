package com.classroom.user.web

import com.classroom.user.service.UserService
import org.apache.commons.logging.LogFactory
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserRestController(private val userService: UserService) {
    private val log = LogFactory.getLog(javaClass)

    @PostMapping("/users/check_email")
    fun checkDuplicateEmail(@Param("userId") userId: Long, @Param("email") email: String): String {
        log.info("received incoming traffic to check email duplication, email: $email")
        return if (userService.isEmailUnique(userId,email)) "OK" else "Duplicated"
    }
}