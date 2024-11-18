package org.smirnovav.tgbotvolatility.service.mainservice.liquidityservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smirnovav.moex_lib.insideutils.DateTimeUtils;

import java.math.BigDecimal;
import java.util.Calendar;

@AllArgsConstructor
@Getter
public class AverageLiquidity {

    private BigDecimal averageVolumeLiquidity;
    private BigDecimal averageValueLiquidity;
    private int maPeriodInDays;
    private String secId;
    private Calendar startDate;
    private Calendar finishDate;

    @Override
    public String toString() {
        return  "Краткий код = " + secId + "\n" +
                "Период усреднения = " + maPeriodInDays + "\n" +
                "Начальная дата = " + DateTimeUtils.calendarToDateWithDash(startDate) + "\n" +
                "Конечная дата = " + DateTimeUtils.calendarToDateWithDash(finishDate) + "\n" +
                "Среднедневное количество сделок (контрактов) за " + maPeriodInDays + " дней = " + averageVolumeLiquidity.longValue() + "\n" +
                "Среднедневной оборот торгов в рублях за " + maPeriodInDays + " дней = " + averageValueLiquidity.longValue() + "\n";
    }
}
