package com.classroom

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClassRoomApplication

fun main(args: Array<String>) {
    runApplication<ClassRoomApplication>(*args)
}