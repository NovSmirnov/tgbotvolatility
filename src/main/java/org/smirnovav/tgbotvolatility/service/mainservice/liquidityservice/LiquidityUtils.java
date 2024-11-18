package org.smirnovav.tgbotvolatility.service.mainservice.liquidityservice;

import org.smirnovav.moex_lib.collectors.futures.FuturesDayHistoryInfo;
import org.smirnovav.moex_lib.utils.SecurityUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class LiquidityUtils {

    private LiquidityUtils() {

    }

    public static AverageLiquidity getAverageLiquidity(String secId, int periodInDays) {
        Calendar lastDate = new GregorianCalendar();
        lastDate.add(Calendar.DATE, -1);
        Calendar firstDate = (Calendar) lastDate.clone();
        firstDate.add(Calendar.DATE, -periodInDays);
        List<FuturesDayHistoryInfo> data = SecurityUtils.getFuturesDailyHistory(secId, firstDate, lastDate);
        BigDecimal totalValue = new BigDecimal("0");
        BigInteger totalVolume = new BigInteger("0");
        for (FuturesDayHistoryInfo info : data) {
            totalValue = totalValue.add(BigDecimal.valueOf(info.getValue()));
            totalVolume = totalVolume.add(BigInteger.valueOf(info.getVolume()));
        }
        BigDecimal averageValue = totalValue.divide(BigDecimal.valueOf(periodInDays), RoundingMode.valueOf(2));
        BigDecimal averageVolume = BigDecimal.valueOf(totalVolume.doubleValue()).divide(BigDecimal.valueOf(periodInDays), RoundingMode.valueOf(2));
        return new AverageLiquidity(averageVolume, averageValue, periodInDays, secId, firstDate, lastDate);
    }

    public static List<AverageLiquidity> getAverageLiquidityList(List<String> secIdList, int periodInDays) {
        List<AverageLiquidity> alList = new ArrayList<>();
        int counter = 0;
        for (String secId : secIdList) {
            counter++;
            System.out.print(counter);
            System.out.print("\r");
            alList.add(getAverageLiquidity(secId, periodInDays));
        }
        return alList;
    }

    public static List<AverageLiquidity> getSomeTopValueLiquidityFutures(int numberOfTopPositions, int periodInDays) {
        Set<String> secIds = SecurityUtils.getAllTradingFutures();
        List<String> secIdList = secIds.stream().toList();
        List<AverageLiquidity> liqList = getAverageLiquidityList(secIdList, periodInDays);
        System.out.println(liqList.size());
        liqList.sort(new AvLiquidityValueComparator());
        Collections.reverse(liqList);
        List<AverageLiquidity> shortSortedList = new ArrayList<>(numberOfTopPositions);
        for (int i = 0; i < numberOfTopPositions; i++) {
            shortSortedList.add(liqList.get(i));
        }
        return shortSortedList;
    }




}
