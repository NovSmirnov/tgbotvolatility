package org.smirnovav.tgbotvolatility.service.mainservice.liquidityservice;


import java.util.Comparator;

public class AvLiquidityVolumeComparator implements Comparator<AverageLiquidity> {
    @Override
    public int compare(AverageLiquidity avLiq1, AverageLiquidity avLiq2) {
        double volume1 = avLiq1.getAverageVolumeLiquidity().doubleValue();
        double volume2 = avLiq2.getAverageVolumeLiquidity().doubleValue();
        return Double.compare(volume1, volume2);
    }
}
