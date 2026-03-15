package com.classroom.user.web

import com.classroom.init.Constants
import com.classroom.init.FileUploadUtil
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
class UserController(private val userService: UserService) {
    private val log: Log = LogFactory.getLog(javaClass)

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
        return listByPage(1, model)
    }

    @RequestMapping("/user/page/{pageNumber}")
    fun listByPage(
        @PathVariable(name = "pageNumber") pageNumber: Int,
        model: Model
    ): String {
        val page = userService.getAllUserDetails(pageNumber)
        val startCount = (pageNumber - 1) * Constants.USERS_PER_PAGE + 1
        val endCount = minOf(startCount + Constants.USERS_PER_PAGE - 1L, page.totalElements)

        model.apply {
            addAttribute("currentPage", pageNumber)
            addAttribute("totalPages", page.totalPages)
            addAttribute("startCount", startCount)
            addAttribute("endCount", endCount)
            addAttribute("totalItems", page.totalElements)
            addAttribute("users", page.content)
        }
        return "user/users"
    }


    @GetMapping("/users/new")
    fun newUser(model: Model): String {
        log.info("received incoming traffic and redirected to new user")
        val user = User()
        user.enabled = true
        model.addAttribute("user", user)
        model.addAttribute("listRoles", userService.listRoles())
        model.addAttribute("pageTitle", "Create New User")
        return "user/user_form"
    }

    @PostMapping("/users/save")
    fun saveUser(
        user: User,
        redirectAttributes: RedirectAttributes,
        @RequestParam("image") multipartFile: MultipartFile
    ): String {
        log.info("Saving user: $user")

        if (!multipartFile.isEmpty) {
            multipartFile.originalFilename?.let { originalFileName ->
                val fileName = StringUtils.cleanPath(originalFileName)
                user.photo = fileName
                val savedUser = userService.save(user)
                val uploadDir = "user-photos/${savedUser.id}"
                FileUploadUtil.cleanDir(uploadDir)
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile)
            }
        } else {
            if (user.photo.isNullOrBlank()) user.photo = null
            userService.save(user)
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.")
        return "redirect:/users"
    }

    @GetMapping("/users/edit/{id}")
    fun editUser(
        @PathVariable id: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        log.info("Editing user with id: $id")
        return runCatching {
            model.apply {
                addAttribute("user", userService.getUserById(id))
                addAttribute("listRoles", userService.listRoles())
                addAttribute("pageTitle", "Edit User (ID: $id)")
            }
            "user/user_form"
        }.getOrElse { e ->
            redirectAttributes.addFlashAttribute("message", e.message)
            "redirect:/users"
        }
    }

    @GetMapping("/users/delete/{id}")
    fun deleteUser(
        @PathVariable id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        log.info("Deleting user with id: $id")
        runCatching {
            userService.delete(id)
            redirectAttributes.addFlashAttribute("message", "The user ID $id has been deleted successfully.")
        }.onFailure { e ->
            redirectAttributes.addFlashAttribute("message", e.message)
        }
        return "redirect:/users"
    }

    @GetMapping("/users/{id}/enabled/{status}")
    fun updateUserEnabledStatus(@PathVariable id: Long, @PathVariable status: Boolean, redirectAttributes: RedirectAttributes): String {
        log.info("received incoming traffic and redirected to update user enabled status")
        userService.updateUserEnabledStatus(id, status)
        val statusMessage = if (status) "enabled" else "disabled"
        redirectAttributes.addFlashAttribute("message", "The user ID $id has been $statusMessage successfully.")
        return "redirect:/users"
    }
}