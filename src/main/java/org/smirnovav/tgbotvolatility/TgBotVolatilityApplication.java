package org.smirnovav.tgbotvolatility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TgBotVolatilityApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TgBotVolatilityApplication.class, args);
    }

}
