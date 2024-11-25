package org.smirnovav.tgbotvolatility.bot;

import org.smirnovav.tgbotvolatility.service.configuration.AppConfig;
import org.smirnovav.tgbotvolatility.service.configuration.Charts;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SimpleBot extends TelegramLongPollingBot {
    private String name;
    private String apiKey;
    private Charts charts;

    private static final String VOLATILITY = "/volatility";



    public SimpleBot(String apiKey, String botName) {
        super(apiKey);
        this.name = botName;
        this.charts = AppConfig.getContext().getBean(Charts.class);
    }

    public String getBotUsername() {
        return name;
    }


    public void onUpdateReceived(Update update) {
        String comment = "Телеграмбот … предназначен для ранжирования 20-ти самых ликвидных фьючерсов, торгующихся на Московской бирже," +
                " по волатильности от наиболее волатильных к наименее волатильным. В качестве измерения волатильности" +
                " используется показатель ATR рассчитанный в процентах к средней цене фьючерса с периодом усреднения" +
                " 7 календарных дней. В описании под графиком выводится актуальное на момент последнего пересчета" +
                " значение показателя. График показывает, как менялся показатель за последние 90 календарных дней." +
                " Это помогает понять складывающиеся тенденции.";

        Message message = update.getMessage();
        try {
            String command = message.getText();
            System.out.println(message.getText());
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(String.valueOf(chatId));
            if (command.equalsIgnoreCase(VOLATILITY)) {
                for (int i = 0; i < charts.getDescription().size(); i++) {
                    SendPhoto photo = new SendPhoto();
                    photo.setChatId(String.valueOf(chatId));
                    photo.setPhoto(charts.getPics().get(i));
                    photo.setCaption(charts.getDescription().get(i));
                    execute(photo);
                }
            } else {
                response.setText("Неверная команда!");
                execute(response);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
