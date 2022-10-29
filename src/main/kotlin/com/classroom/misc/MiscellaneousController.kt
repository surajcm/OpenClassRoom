package com.classroom.misc

import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MiscellaneousController {
    private val log = LogFactory.getLog(javaClass)

    @GetMapping("/plans")
    fun welcome(): String {
        log.info("received incoming traffic and redirected to plans")
        return "misc/plans"
    }

    @GetMapping("/faq")
    fun faq(): String {
        log.info("received incoming traffic and redirected to faq")
        return "misc/faq"
    }

    @GetMapping("/help/support")
    fun support(): String {
        log.info("received incoming traffic and redirected to support")
        return "misc/support"
    }
}