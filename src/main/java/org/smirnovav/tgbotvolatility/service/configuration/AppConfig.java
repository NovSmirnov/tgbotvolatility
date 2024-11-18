package org.smirnovav.tgbotvolatility.service.configuration;

import org.smirnovav.tgbotvolatility.bot.SimpleBot;
import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;
import org.smirnovav.tgbotvolatility.service.mainservice.utils.IOUtils;
import org.smirnovav.tgbotvolatility.service.mainservice.utils.MessageUtils;
import org.smirnovav.tgbotvolatility.service.mainservice.utils.VolatilityBotService;
import org.smirnovav.tgbotvolatility.service.mainservice.volatility_service.VolatilityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Configuration
public class AppConfig {
    private final SimpleBotConfig simpleBotConfig;
    private static ApplicationContext context;
    private ChartParametersConfig chartParametersConfig;


    @Autowired
    public AppConfig(SimpleBotConfig botConfig, ApplicationContext applicationContext, ChartParametersConfig chartParametersConfig) {
        this.simpleBotConfig = botConfig;
        context = applicationContext;
        this.chartParametersConfig = chartParametersConfig;
    }


    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        SimpleBot simpleBot = new SimpleBot(simpleBotConfig.getBotToken(), simpleBotConfig.getBotUserName());
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(simpleBot);
        return api;
    }

    @Bean
    public Charts charts() {
        return getNewCharts();
    }

    public static ApplicationContext getContext() {
        return context;
    }

    @Scheduled(cron = "0 30 4 * * *")
    public void setNewCharts() {
        Charts charts = context.getBean(Charts.class);
        Charts newCharts = getNewCharts();
        charts.setDescription(newCharts.getDescription());
        charts.setPics(newCharts.getPics());
    }

    private Charts getNewCharts() {
        VolatilityBotService.updateAndStoreResult();
        List<IntegratedFutVolLiq> integratedFutVolLiqs = VolatilityBotService.loadResult();
        List<List<IntegratedFutVolLiq>> doubleListsFutVolLiq = VolatilityUtils.createSubLists(integratedFutVolLiqs,
                chartParametersConfig.getLinesInChart());
        List<String> description = MessageUtils.getVolLiqMessages(doubleListsFutVolLiq);
        IOUtils.cleanDirectory(chartParametersConfig.getImagesDirectory() + "\\temp");
        List<InputFile> pics = IOUtils.getInputMultiLinesFiles(doubleListsFutVolLiq, chartParametersConfig.getImagesDirectory());
        return new Charts(description, pics);
    }


}
