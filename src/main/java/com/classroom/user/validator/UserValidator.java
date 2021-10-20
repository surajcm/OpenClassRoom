package com.classroom.user.validator;

import com.classroom.user.domain.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    /**
     * logger for validator.
     */
    private final Log log = LogFactory.getLog(UserValidator.class);

    /**
     * default method overridden for matching the POJO.
     * @param aClass aClass instance
     * @return boolean
     */
    public boolean supports(final Class<?> aClass) {
        return aClass.equals(UserVO.class);
    }

    /**
     * validate method overridden from validator.
     * @param obj o the user instance
     * @param errors errors
     */
    public void validate(final Object obj, final Errors errors) {
        log.info(" Inside the validate method");
        var user = (UserVO) obj;

        if (user.getName().trim().length() == 0) {
            errors.rejectValue("name", "error.required.user.name", "User Name is required");
        }
        if (user.getPassword().trim().length() == 0) {
            errors.rejectValue("password", "error.required.password", "Password is required");
        }
    }
}
