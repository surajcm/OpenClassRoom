package com.classroom.user.service

interface SecurityService {
    fun findLoggedInUsername(): String?
}