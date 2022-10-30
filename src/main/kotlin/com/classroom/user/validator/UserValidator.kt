package com.classroom.user.validator

import com.classroom.user.domain.UserVO
import org.apache.commons.logging.LogFactory
import org.springframework.validation.Errors
import org.springframework.validation.Validator

class UserValidator: Validator {
    private val log = LogFactory.getLog(javaClass)

    override fun supports(aClass: Class<*>): Boolean {
        return aClass == UserVO::class.java
    }

    override fun validate(obj: Any, errors: Errors) {
        log.info(" Inside the validate method")
        val user = obj as UserVO
        if (user.email?.trim { it <= ' ' }?.length  == 0) {
            errors.rejectValue("name", "error.required.user.name", "User Name is required")
        }
        if (user.password?.trim { it <= ' ' }?.length == 0) {
            errors.rejectValue("password", "error.required.password", "Password is required")
        }
    }
}