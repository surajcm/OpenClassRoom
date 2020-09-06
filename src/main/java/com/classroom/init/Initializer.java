package com.classroom.init;

import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Suraj Muraleedharan
 * on 1/9/17.
 */
@Component
public class Initializer {
    @Value("${hsql.ui.disabled}")
    private Boolean isUIDisabled;

    /**
     * This is to run a sql manager that comes up with hsqldb, while starting the app.
     */
    @PostConstruct
    public void init() {
        if (!isUIDisabled) {
            System.setProperty("java.awt.headless", "false");
            DatabaseManagerSwing.main(
                    new String[]{"--url", "jdbc:hsqldb:mem:testdb", "--user", "sa", "--password", ""});
        }
    }

}
