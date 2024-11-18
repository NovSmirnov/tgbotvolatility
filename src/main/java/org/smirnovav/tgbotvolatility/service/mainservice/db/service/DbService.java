package org.smirnovav.tgbotvolatility.service.mainservice.db.service;

import org.smirnovav.moex_lib.insideutils.DateTimeUtils;
import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;
import org.smirnovav.tgbotvolatility.service.mainservice.db.dao.Dao;
import org.smirnovav.tgbotvolatility.service.mainservice.db.dbutils.HibernateSessionFactoryUtil;
import org.smirnovav.tgbotvolatility.service.mainservice.db.entities.IntegratedFutVolLiqEntity;
import org.smirnovav.tgbotvolatility.service.mainservice.db.mappers.IntegratedFutVolLiqMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DbService {
    private DbService() {
    }

    public static void saveVolatilityResultsToBase(List<IntegratedFutVolLiq> integratedFutVolLiqList) {
        Dao dao = new Dao();
        List<IntegratedFutVolLiqEntity> integratedFutVolLiqEntities = new ArrayList<>();
        for (int i = 0; i < integratedFutVolLiqList.size(); i++) {
            integratedFutVolLiqEntities.add(IntegratedFutVolLiqMapper.integratedFutVolLiqToEntity(integratedFutVolLiqList.get(i)));
        }
        dao.saveAll(integratedFutVolLiqEntities);
    }

    public static List<IntegratedFutVolLiq> loadVolatilityResultsFromBase() {
        Dao dao = new Dao();
        List<IntegratedFutVolLiq> integratedFutVolLiqList = new ArrayList<>();
        List<IntegratedFutVolLiqEntity> integratedFutVolLiqEntities = dao.loadAll();
        for (int i = 0; i < integratedFutVolLiqEntities.size(); i++) {
            integratedFutVolLiqList.add(IntegratedFutVolLiqMapper.entityToIntegratedFutVolLiq(integratedFutVolLiqEntities.get(i)));
        }
        return integratedFutVolLiqList;
    }

    /**
     * Проверяет актуальность данных записанных в базу данных (данные актуальны, если запись была по состоянию на вчера
     * или последнюю пятницу, если сегодня понедельник
     * @return true - если база не актуальна, false - база актуальна
     */
    public static boolean lastDateIsNotYesterday() {
        if (HibernateSessionFactoryUtil.isBaseExist()) {
            Calendar yesterday = new GregorianCalendar();
            yesterday.add(Calendar.DATE, -1);
            List<IntegratedFutVolLiq> integratedFutVolLiqs = loadVolatilityResultsFromBase();
            Calendar lastDate = integratedFutVolLiqs.get(0).getFinishDate();
            if (yesterday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                yesterday.add(Calendar.DATE, -2);
            } else if (yesterday.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                yesterday.add(Calendar.DATE, -1);
            }
            if (DateTimeUtils.calendarToDate(yesterday).equals(DateTimeUtils.calendarToDate(lastDate))) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


}
