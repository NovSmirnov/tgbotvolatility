package org.smirnovav.tgbotvolatility.service.mainservice.volatility_service;

import org.smirnovav.moex_lib.collectors.futures.FuturesBoardSpecification;
import org.smirnovav.moex_lib.collectors.futures.FuturesDayHistoryInfo;
import org.smirnovav.moex_lib.utils.SecurityUtils;
import org.smirnovav.tgbotvolatility.service.mainservice.AtrIndicator;
import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;
import org.smirnovav.tgbotvolatility.service.mainservice.collectors.comparators.IntegratedVolatilityValueComparator;
import org.smirnovav.tgbotvolatility.service.mainservice.liquidityservice.AverageLiquidity;
import org.smirnovav.tgbotvolatility.service.mainservice.liquidityservice.LiquidityUtils;

import java.util.*;

public class VolatilityUtils {
    private VolatilityUtils() {
    }

    public static AtrPrc getOneAtrPrc(String secId, int maPeriod, Calendar firstDate, Calendar lastDate) {
        List<FuturesDayHistoryInfo> quote = SecurityUtils.getFuturesDailyHistory(secId, firstDate, lastDate);
        double[] atr = AtrIndicator.atrInPrcWithoutZerosFromMoexCandleList(quote, maPeriod);
        return AtrPrc.builder()
                .values(atr)
                .secId(secId)
                .maPeriodInDays(maPeriod)
                .startDate(firstDate)
                .finishDate(lastDate)
                .build();
    }

    public static List<AtrPrc> getAllMoexFuturesVolatility(int maPeriod, Calendar firstDate, Calendar lastDate) {
        Set<String> secIds = SecurityUtils.getAllTradingFutures();
        List<AtrPrc> volatilityList = new ArrayList<>();
        for (String secId : secIds) {
            volatilityList.add(getOneAtrPrc(secId, maPeriod, firstDate, lastDate));
        }
        return volatilityList;
    }

    public static List<AtrPrc> getSomeTopVolatilityFutures(int numberOfTopPositions, int maPeriod, Calendar firstDate,
                                                           Calendar lastDate) {
        List<AtrPrc> futures = getAllMoexFuturesVolatility(maPeriod, firstDate, lastDate);
        System.out.println(futures.size());
        futures.sort(new AtrPrcVolComparator());
        Collections.reverse(futures);
        List<AtrPrc> shortSortedList = new ArrayList<>(numberOfTopPositions);
        for (int i = 0; i < numberOfTopPositions; i++) {
            shortSortedList.add(futures.get(i));
        }
        return shortSortedList;
    }

    /**
     * Создает сортированный список наиболее волатильных фьючерсов Мосбиржи среди наиболее ликвидных
     * @param numberOfTopPositions Количество элементов в списке самых ликвидных
     * @param maPeriod Период усреднения для индикатора ATR
     * @param periodInDays Период для вычисления среднедневной ликвидности
     * @param firstDate Начальная дата проведения расчетов
     * @param lastDate Конечная дата проведения расчетов
     * @return Сортированный список наиболее волатильных фьючерсов Мосбиржи среди наиболее ликвидных
     */
    public static List<IntegratedFutVolLiq> futuresVolatilityFromLiquidity(int numberOfTopPositions, int maPeriod, int periodInDays,
                                                                           Calendar firstDate, Calendar lastDate) {
        List<AverageLiquidity> topLiquidity = LiquidityUtils.getSomeTopValueLiquidityFutures(numberOfTopPositions, periodInDays);
        List<IntegratedFutVolLiq> integratedFutVolLiqList = new ArrayList<>();
        for (AverageLiquidity averageLiquidity : topLiquidity) {
            AtrPrc atrPrc = getOneAtrPrc(averageLiquidity.getSecId(), maPeriod, firstDate, lastDate);
            FuturesBoardSpecification info = SecurityUtils.getFuturesComplexInfo(atrPrc.getSecId()).getSpecification();
            integratedFutVolLiqList.add(new IntegratedFutVolLiq(atrPrc.getValues(), atrPrc.getMaPeriodInDays(),
                    atrPrc.getSecId(), info.getShortName(), info.getAssetCode(), firstDate, lastDate,
                    averageLiquidity.getAverageVolumeLiquidity(), averageLiquidity.getAverageValueLiquidity()));
        }
        integratedFutVolLiqList.sort(new IntegratedVolatilityValueComparator());
        Collections.reverse(integratedFutVolLiqList);
        return integratedFutVolLiqList;
    }

    /**
     * Создает сортированный список наиболее волатильных фьючерсов Мосбиржи среди наиболее ликвидных
     * @param numberOfTopPositions Количество элементов в списке самых ликвидных
     * @param maPeriod Период усреднения для индикатора ATR
     * @param periodInDays Период для вычисления среднедневной ликвидности
     * @param duration Количество дней за которые берется история инструментов, последний день истории - вчерашний день
     * @return Сортированный список наиболее волатильных фьючерсов Мосбиржи среди наиболее ликвидных
     */
    public static List<IntegratedFutVolLiq> actualFutVolatilityFromLiquidity(int numberOfTopPositions, int maPeriod,
                                                                             int periodInDays, int duration) {
        Calendar today = new GregorianCalendar();
        Calendar lastDate = (Calendar) today.clone();
        lastDate.add(Calendar.DATE, -1);
        Calendar firstDate = (Calendar) lastDate.clone();
        firstDate.add(Calendar.DATE, -duration);
        return futuresVolatilityFromLiquidity(numberOfTopPositions, maPeriod, periodInDays, firstDate, lastDate);
    }

    /**
     * Из одного списка создает несколько подсписков с установленным количеством элементов, если оно меньше, чем
     * количество элементов исходного списка.
     * @param integratedFutVolLiqList Список объектов integratedFutVolLiqList
     * @param numberOfElements Максимальное количество элементов в одном подсписке
     * @return Список списков объектов integratedFutVolLiqList
     */
    public static List<List<IntegratedFutVolLiq>> createSubLists(List<IntegratedFutVolLiq> integratedFutVolLiqList,
                                                                 int numberOfElements) {
        if (integratedFutVolLiqList.isEmpty() || numberOfElements <= 0) {
            return null;
        }
        if (integratedFutVolLiqList.size() >= numberOfElements) {
            List<List<IntegratedFutVolLiq>> shortLists = new ArrayList<>();
            for (int i = 0; i < integratedFutVolLiqList.size(); i+= numberOfElements) {
                List<IntegratedFutVolLiq> subList = integratedFutVolLiqList.subList(i, i + numberOfElements);
                shortLists.add(subList);
            }
            if (integratedFutVolLiqList.size() % numberOfElements != 0) {
                List<IntegratedFutVolLiq> subList = integratedFutVolLiqList.subList(integratedFutVolLiqList.size() -
                        (integratedFutVolLiqList.size() % numberOfElements), integratedFutVolLiqList.size());
                shortLists.add(subList);
            }
            return shortLists;
        } else {
            List<List<IntegratedFutVolLiq>> shortLists = new ArrayList<>();
            shortLists.add(integratedFutVolLiqList);
            return shortLists;
        }
    }
}
