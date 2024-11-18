package org.smirnovav.tgbotvolatility.service.mainservice.collectors.comparators;

import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;

import java.util.Comparator;

public class IntegratedVolatilityValueComparator implements Comparator<IntegratedFutVolLiq> {
    @Override
    public int compare(IntegratedFutVolLiq obj1, IntegratedFutVolLiq obj2) {
        return Double.compare(obj1.getValues()[obj1.getValues().length - 1], obj2.getValues()[obj2.getValues().length - 1]);
    }
}
