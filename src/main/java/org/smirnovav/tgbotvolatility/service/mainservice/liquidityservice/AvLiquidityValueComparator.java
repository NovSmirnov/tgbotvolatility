package org.smirnovav.tgbotvolatility.service.mainservice.liquidityservice;


import java.util.Comparator;

public class AvLiquidityValueComparator implements Comparator<AverageLiquidity> {
    @Override
    public int compare(AverageLiquidity avLiq1, AverageLiquidity avLiq2) {
        double value1 = avLiq1.getAverageValueLiquidity().doubleValue();
        double value2 = avLiq2.getAverageValueLiquidity().doubleValue();
        return Double.compare(value1, value2);
    }
}
