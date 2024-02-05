package com.classroom.user.web

import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.logging.LogFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class UserController {
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
}