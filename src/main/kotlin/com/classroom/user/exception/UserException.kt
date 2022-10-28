package com.classroom.user.exception

class UserException(databaseError: String) : Exception() {

    var exceptionType: String? = null

    fun UserException(exceptionType: String) {
        this.exceptionType = exceptionType
    }
}