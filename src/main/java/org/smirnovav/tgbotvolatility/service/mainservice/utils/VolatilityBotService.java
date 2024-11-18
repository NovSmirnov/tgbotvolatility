package org.smirnovav.tgbotvolatility.service.mainservice.utils;

import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;
import org.smirnovav.tgbotvolatility.service.mainservice.db.service.DbService;
import org.smirnovav.tgbotvolatility.service.mainservice.volatility_service.VolatilityUtils;

import java.util.List;

public class VolatilityBotService {

    /**
     * Получает и записывает в базу данных результат метода getAndStoreResult(), если база не актуальна, если актуальна,
     * то ничего не делает
     */
    public static void updateAndStoreResult() {
        if (DbService.lastDateIsNotYesterday()) {
            getAndStoreResult();
        }
    }

    /**
     * Получает и записывает в базу данных результат: 20 наиболее ликвидных фьючерсов Мосбиржи (ликвидность вычисляется
     * как среднедневной объем сделок за последние 20 календарных дней) отсортированных в обратном порядке по волатильности
     * (показатель ATR в процентах с усреднением 7 календарных дней)
     */
    private static void getAndStoreResult() {
        List<IntegratedFutVolLiq> result = VolatilityUtils.actualFutVolatilityFromLiquidity(20, 7,
                20, 90);
        DbService.saveVolatilityResultsToBase(result);
    }

    /**
     * Загружает из базы данных информацию о самых волатильных фьючерсах среди самых ликвидных
     * @return Список объектов класса IntegratedFutVolLiq, содержащих информацию о самых волатильных фьючерсах среди самых ликвидных
     */
    public static List<IntegratedFutVolLiq> loadResult() {
        return DbService.loadVolatilityResultsFromBase();
    }


    

}
