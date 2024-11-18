package org.smirnovav.tgbotvolatility.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ChartParametersConfig {
    @Value("${images_directory}")
    private String imagesDirectory;
    @Value("${lines_in_chart}")
    private int linesInChart;
}
