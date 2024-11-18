package org.smirnovav.tgbotvolatility.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.List;

@Component
@Getter
@Setter
public class Charts {
    private List<String> description;
    private List<InputFile> pics;

    public Charts(List<String> description, List<InputFile> pics) {
        this.description = description;
        this.pics = pics;
    }
}
