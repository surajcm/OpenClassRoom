package com.classroom.user.service

interface SecurityService {
    fun findLoggedInUsername(): String?

    fun autologin(username: String?, password: String?)
}