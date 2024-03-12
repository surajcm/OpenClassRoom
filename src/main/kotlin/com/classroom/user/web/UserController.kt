package com.classroom.user.web

import com.classroom.init.Constants
import com.classroom.init.FileUploadUtil
import com.classroom.user.dao.impl.entities.User
import com.classroom.user.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.logging.LogFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
class UserController(
    private val userService: UserService
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
        return listByPage(1, model);
    }

    @RequestMapping("/user/page/{pageNumber}")
    fun listByPage(@PathVariable(name = "pageNumber") pageNumber: Int,
        model: Model
    ): String {
        val page = userService.getAllUserDetails(pageNumber)
        val startCount = (pageNumber - 1) * Constants.USERS_PER_PAGE+ 1
        var endCount: Long = startCount.toLong() + Constants.USERS_PER_PAGE - 1
        if (endCount > page.totalElements) {
            endCount = page.totalElements
        }
        model.addAttribute("currentPage", pageNumber)
        model.addAttribute("totalPages", page.totalPages)
        model.addAttribute("startCount", startCount)
        model.addAttribute("endCount", endCount)
        model.addAttribute("totalItems", page.totalElements)
        model.addAttribute("users", page.content)
        return "user/users"
    }


    @GetMapping("/users/new")
    fun newUser(model: Model): String {
        log.info("received incoming traffic and redirected to new user")
        val user = User()
        user.enabled = true
        model.addAttribute("user", user)
        model.addAttribute("listRoles", userService.listRoles())
        model.addAttribute("pageTitle", "Create New User");
        return "user/user_form"
    }

    @PostMapping("/users/save")
    fun saveUser(user: User, redirectAttributes: RedirectAttributes,
                 @RequestParam("image") multipartFile: MultipartFile): String {
        log.info("received incoming traffic and redirected to save user "+ user.toString())
        if (!multipartFile.isEmpty) {
            val fileName = StringUtils.cleanPath(multipartFile.originalFilename!!)
            user.photo = fileName
            val savedUser = userService.save(user)
            val uploadDir = "user-photos/" + savedUser.id
            FileUploadUtil().cleanDir(uploadDir)
            FileUploadUtil().saveFile(uploadDir, fileName, multipartFile)
        } else {
            if (user.photo?.isEmpty() == true) user.photo = null
            userService.save(user)
        }
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.")
        return "redirect:/users"
    }

    @GetMapping("/users/edit/{id}")
    fun editUser(@PathVariable id: Long, model: Model, redirectAttributes: RedirectAttributes): String {
        log.info("received incoming traffic and redirected to edit user")
        try {
            model.addAttribute("user", userService.getUserById(id))
            model.addAttribute("listRoles", userService.listRoles())
            model.addAttribute("pageTitle", "Edit User (ID: $id)");
            return "user/user_form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("message", e.message)
            return "redirect:/users"
        }
    }

    @GetMapping("/users/delete/{id}")
    fun deleteUser(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        log.info("received incoming traffic and redirected to delete user")
        try {
            userService.delete(id)
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully.")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("message", e.message)
        }
        return "redirect:/users"
    }

    @GetMapping("/users/{id}/enabled/{status}")
    fun updateUserEnabledStatus(@PathVariable id: Long, @PathVariable status: Boolean, redirectAttributes: RedirectAttributes): String {
        log.info("received incoming traffic and redirected to update user enabled status")
        userService.updateUserEnabledStatus(id, status)
        val statusMessage = if (status) "enabled" else "disabled"
        redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been " + statusMessage + " successfully.")
        return "redirect:/users"
    }
}