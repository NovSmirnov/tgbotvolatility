package org.smirnovav.tgbotvolatility.service.mainservice.collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smirnovav.moex_lib.insideutils.DateTimeUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;

@AllArgsConstructor
@Getter
public class IntegratedFutVolLiq {
    private double[] values; // Значения показателя ATR в процентах
    private int maPeriodInDays; // Период усреднения показателя в днях
    private String secId; // Краткий код биржевого инструмента
    private String shortName; // Сокращенное имя биржевого инструмента
    private String assetCode; // База краткого наименования контракта
    private Calendar startDate; // Начальная дата периода времени
    private Calendar finishDate; // Конечная дата периода времени
    private BigDecimal averageVolumeLiquidity; // Среднедневная ликвидность в количестве сделок (лотов)
    private BigDecimal averageValueLiquidity; // Среднедневная ликвидность в рублях

    @Override
    public String toString() {
        return  "Краткий код = " + secId + "\n" +
                "Короткое наименование инструмента = " + shortName + "\n" +
                "База краткого наименования контракта = " + assetCode + "\n" +
                "Период усреднения = " + maPeriodInDays + "\n" +
                "Начальная дата = " + DateTimeUtils.calendarToDateWithDash(startDate) + "\n" +
                "Конечная дата = " + DateTimeUtils.calendarToDateWithDash(finishDate) + "\n" +
                "Среднедневное количество сделок (контрактов) за " + maPeriodInDays + " дней = " + averageVolumeLiquidity.longValue() + "\n" +
                "Среднедневной оборот торгов в рублях за " + maPeriodInDays + " дней = " + averageValueLiquidity.longValue() + "\n" +
                "Значения показателя ATR = " + Arrays.toString(values) + "\n";
    }

    public String getMessage() {
        return "Фьючерс: "  + shortName + "\n" +
                "\n" +
                "Период усреднения" + "\n" +
                "показателя ATR,% в днях: " + maPeriodInDays + "\n" +
                "\n" +
                "Текущий ATR, %: " + new DecimalFormat("###.##").format(values[values.length - 1]) + "\n" +
                "\n" +
                "Начальная дата: " + DateTimeUtils.calendarToDateWithDash(startDate) + "\n" +
                "Конечная дата: " + DateTimeUtils.calendarToDateWithDash(finishDate) + "\n" +
                "\n" +
                "Среднедневное количество сделок" + "\n" +
                "(контрактов) за " + maPeriodInDays +
                " дней: " + new DecimalFormat("###,###,###,###,###").format(averageVolumeLiquidity.longValue()) + "\n" +
                "\n" +
                "Среднедневной оборот торгов" + "\n" +
                "в рублях за " + maPeriodInDays +
                " дней: " + new DecimalFormat("###,###,###,###,###").format(averageValueLiquidity.longValue()) + "\n";
    }
}
