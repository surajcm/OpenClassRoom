package com.classroom.user.exception;

@SuppressWarnings("unused")
public class UserException extends Exception {

    /**
     * Exception type for Unknown user.
     */
    public static final String UNKNOWN_USER = "UNKNOWN_USER";

    /**
     * exception type for Invalid user.
     */
    public static final String INCORRECT_PASSWORD = "INCORRECT_PASSWORD";

    /**
     * exception type for all database related errors.
     */
    public static final String DATABASE_ERROR = "DATABASE_ERROR";

    public String exceptionType;

    public UserException(final String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(final String exceptionType) {
        this.exceptionType = exceptionType;
    }
}
