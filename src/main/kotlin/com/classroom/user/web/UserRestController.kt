package com.classroom.user.web

import com.classroom.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserRestController(private val userService: UserService) {
    @PostMapping("/users/check_email")
    fun checkDuplicateEmail(email: String): String {
        return if (userService.isEmailUnique(email)) "OK" else "Duplicated"
    }
}