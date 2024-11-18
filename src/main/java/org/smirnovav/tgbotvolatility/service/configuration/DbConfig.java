package org.smirnovav.tgbotvolatility.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DbConfig {

    @Value("${hibernate.connection.driver_class}")
    private String driver;
    @Value("${hibernate.connection.url}")
    private String url;
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${show_sql}")
    private String showSQL;


}
