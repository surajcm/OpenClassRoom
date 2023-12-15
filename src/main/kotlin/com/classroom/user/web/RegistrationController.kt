package com.classroom.user.web

import com.classroom.user.domain.UserVO
import com.classroom.user.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/registration")
class RegistrationController(private val userService: UserService) {
    @ModelAttribute("userVO")
    fun userVO(): UserVO {
        return UserVO()
    }

    @GetMapping
    fun showRegistrationForm(): String {
        return "registration"
    }

    @PostMapping
    fun registerUserAccount(@ModelAttribute("user") user: UserVO?): String {
        userService.save(user)
        return "redirect:/registration?success"
    }
}