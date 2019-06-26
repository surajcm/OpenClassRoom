package com.classroom.misc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiscellaneousController {
    private final Log log = LogFactory.getLog(MiscellaneousController.class);

    @GetMapping("/plans")
    public String welcome(Model model) {
        log.info("received incoming traffic and redirected to plans");
        return "misc/plans";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        log.info("received incoming traffic and redirected to faq");
        return "misc/faq";
    }

    @GetMapping("/help/support")
    public String support(Model model) {
        log.info("received incoming traffic and redirected to support");
        return "misc/support";
    }
}
