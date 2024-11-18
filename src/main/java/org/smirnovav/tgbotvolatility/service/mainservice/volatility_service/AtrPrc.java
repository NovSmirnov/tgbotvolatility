package org.smirnovav.tgbotvolatility.service.mainservice.volatility_service;

import lombok.Builder;
import lombok.Getter;
import org.smirnovav.moex_lib.insideutils.DateTimeUtils;

import java.util.Arrays;
import java.util.Calendar;

@Builder
@Getter
public class AtrPrc {

    private double[] values; // Значения показателя ATR в процентах
    private int maPeriodInDays; // Период усреднения показателя в днях
    private String secId; // Краткий код биржевого инструмента
    private Calendar startDate; // Начальная дата периода времени
    private Calendar finishDate; // Конечная дата периода времени

    @Override
    public String toString() {
        return  "Краткий код = " + secId + "\n" +
                "Период усреднения = " + maPeriodInDays + "\n" +
                "Начальная дата = " + DateTimeUtils.calendarToDateWithDash(startDate) + "\n" +
                "Конечная дата = " + DateTimeUtils.calendarToDateWithDash(finishDate) + "\n" +
                "Значения показателя ATR = " + Arrays.toString(values) + "\n";
    }
}
