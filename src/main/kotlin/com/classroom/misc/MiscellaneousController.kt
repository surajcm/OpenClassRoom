package com.classroom.misc

import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MiscellaneousController {
    private val log = LogFactory.getLog(MiscellaneousController::class.java)

    @GetMapping("/plans")
    fun welcome(model: Model?): String? {
        log.info("received incoming traffic and redirected to plans")
        return "misc/plans"
    }

    @GetMapping("/faq")
    fun faq(model: Model?): String? {
        log.info("received incoming traffic and redirected to faq")
        return "misc/faq"
    }

    @GetMapping("/help/support")
    fun support(model: Model?): String? {
        log.info("received incoming traffic and redirected to support")
        return "misc/support"
    }
}