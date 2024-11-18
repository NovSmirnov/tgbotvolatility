package org.smirnovav.tgbotvolatility.service.mainservice.utils;

import org.smirnovav.moex_lib.insideutils.DateTimeUtils;
import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageUtils {
    private MessageUtils(){}

    public static List<String> getVolLiqMessages(List<List<IntegratedFutVolLiq>> integratedFutVolLiqLists) {
        List<String> messages = new ArrayList<>();
        int counter = 1;
        for (List<IntegratedFutVolLiq> integratedFutVolLiqs : integratedFutVolLiqLists) {
            StringBuilder builder = new StringBuilder();
            builder.append("Период усреднения\nпоказателя ATR,% в днях: ")
                    .append(integratedFutVolLiqs.get(0).getMaPeriodInDays()).append("\n")
                    .append("Начальная дата: ").append(DateTimeUtils.calendarToDateWithDash(integratedFutVolLiqs.get(0).getStartDate()))
                    .append("\n")
                    .append("Конечная дата: ").append(DateTimeUtils.calendarToDateWithDash(integratedFutVolLiqs.get(0).getFinishDate()))
                    .append("\n\n");


            for (int i = 0; i < integratedFutVolLiqs.size(); i++) {
                builder.append(counter).append(". ")
                        .append("Фьючерс: ").append(integratedFutVolLiqs.get(i).getShortName()).append("\n")
                        .append("Текущий ATR, %: ")
                        .append(new DecimalFormat("###.##").format(integratedFutVolLiqs.get(i)
                                .getValues()[integratedFutVolLiqs.get(i).getValues().length - 1])).append("\n")
                        .append("Среднедневное количество сделок").append("\n")
                        .append("(контрактов) за ").append(integratedFutVolLiqs.get(i).getMaPeriodInDays()).append(" дней: ")
                        .append(new DecimalFormat("###,###,###,###,###").format(integratedFutVolLiqs.get(i).getAverageVolumeLiquidity().longValue()))
                        .append("\n")
                        .append("Среднедневной оборот торгов").append("\n")
                        .append("в рублях за ").append(integratedFutVolLiqs.get(i).getMaPeriodInDays()).append(" дней: ")
                        .append(new DecimalFormat("###,###,###,###,###").format(integratedFutVolLiqs.get(i).getAverageValueLiquidity().longValue()))
                        .append("\n\n");
                counter++;
            }
            messages.add(builder.toString());
        }
        return messages;
    }
}
