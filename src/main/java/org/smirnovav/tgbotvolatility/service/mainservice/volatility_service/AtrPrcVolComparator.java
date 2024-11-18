package org.smirnovav.tgbotvolatility.service.mainservice.volatility_service;

import java.util.Comparator;

public class AtrPrcVolComparator implements Comparator<AtrPrc> {
    @Override
    public int compare(AtrPrc atrPrc1, AtrPrc atrPrc2) {
        double[] values1 = atrPrc1.getValues();
        double[] values2 = atrPrc2.getValues();
        if (values1.length == 0 && values2.length != 0) {
            return -1;
        } else if (values1.length != 0 && values2.length == 0) {
            return 1;
        } else if (values1.length == 0 && values2.length == 0) {
            return 0;
        } else {
            return Double.compare(atrPrc1.getValues()[atrPrc1.getValues().length - 1], atrPrc2.getValues()[atrPrc2.getValues().length - 1]);
        }
    }
}
