package org.smirnovav.tgbotvolatility.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SimpleBotConfig {
    @Value("${bot.name}")
    private String botUserName;
    @Value("${bot.api.key}")
    private String botToken;

}
