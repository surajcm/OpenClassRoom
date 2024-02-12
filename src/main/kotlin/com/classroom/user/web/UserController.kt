package com.classroom.user.web

import com.classroom.user.dao.impl.entities.User
import com.classroom.user.service.impl.UserServiceImpl
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.logging.LogFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
class UserController(
    private val userService: UserServiceImpl
) {
    /**
     * logger for user controller.
     */
    private val log = LogFactory.getLog(javaClass)

    @GetMapping(value = ["/", "/welcome"])
    fun welcome(): String {
        log.info("received incoming traffic and redirected to welcome")
        return "welcome"
    }

    @GetMapping(value = ["/login"])
    fun login(model: Model, error: String?, logout: String?): String {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.")
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.")
        }
        return "login"
    }

    @GetMapping(value = ["/user/preferences"])
    fun preference(): String {
        log.info("received incoming traffic and redirected to preferences")
        return "user/preferences"
    }

    @GetMapping("/user/logMeOut")
    fun logMeOut(request: HttpServletRequest): String {
        log.info("Inside LogMeOut method of user controller")
        SecurityContextHolder.clearContext()
        request.getSession(false)?.invalidate()
        return "login"
    }

    @GetMapping("/users")
    fun listAll(model: Model): String {
        log.info("received incoming traffic and redirected to users")
        model.addAttribute("users", userService.getAllUserDetails())
        return "user/users"
    }

    @GetMapping("/users/new")
    fun newUser(model: Model): String {
        log.info("received incoming traffic and redirected to new user")
        val user = User()
        user.enabled = true
        model.addAttribute("user", user)
        model.addAttribute("listRoles", userService.listRoles())
        return "user/user_form"
    }

    @PostMapping("/users/save")
    fun saveUser(user: User, redirectAttributes: RedirectAttributes): String {
        log.info("received incoming traffic and redirected to save user "+ user.toString())
        userService.save(user)
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.")
        return "redirect:/users"
    }
}