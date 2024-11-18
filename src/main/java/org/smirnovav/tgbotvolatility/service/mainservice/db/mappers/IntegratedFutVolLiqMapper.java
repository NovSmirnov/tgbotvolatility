package org.smirnovav.tgbotvolatility.service.mainservice.db.mappers;

import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;
import org.smirnovav.tgbotvolatility.service.mainservice.db.entities.AtrValueEntity;
import org.smirnovav.tgbotvolatility.service.mainservice.db.entities.IntegratedFutVolLiqEntity;

import java.util.ArrayList;
import java.util.List;

public class IntegratedFutVolLiqMapper {
    private IntegratedFutVolLiqMapper() {
    }

    public static IntegratedFutVolLiqEntity integratedFutVolLiqToEntity(IntegratedFutVolLiq integratedFutVolLiq) {
        List<AtrValueEntity> atrValueEntities = new ArrayList<>();
        for (int i = 0; i < integratedFutVolLiq.getValues().length; i++) {
            atrValueEntities.add(AtrValueEntity.builder()
                    .value(integratedFutVolLiq.getValues()[i])
                    .valueNumber(i)
                    .build());
        }
        return IntegratedFutVolLiqEntity.builder()
                .maPeriodInDays(integratedFutVolLiq.getMaPeriodInDays())
                .secId(integratedFutVolLiq.getSecId())
                .shortName(integratedFutVolLiq.getShortName())
                .assetCode(integratedFutVolLiq.getAssetCode())
                .startDate(integratedFutVolLiq.getStartDate())
                .finishDate(integratedFutVolLiq.getFinishDate())
                .averageVolumeLiquidity(integratedFutVolLiq.getAverageVolumeLiquidity())
                .averageValueLiquidity(integratedFutVolLiq.getAverageValueLiquidity())
                .values(atrValueEntities)
                .build();
    }

    public static IntegratedFutVolLiq entityToIntegratedFutVolLiq(IntegratedFutVolLiqEntity integratedFutVolLiqEntity) {
        double[] values = new double[integratedFutVolLiqEntity.getValues().size()];
        for (int i = 0; i < values.length; i++) {
            int index = integratedFutVolLiqEntity.getValues().get(i).getValueNumber();
            values[index] = integratedFutVolLiqEntity.getValues().get(i).getValue();
        }
        return new IntegratedFutVolLiq(values, integratedFutVolLiqEntity.getMaPeriodInDays(),
                integratedFutVolLiqEntity.getSecId(), integratedFutVolLiqEntity.getShortName(),
                integratedFutVolLiqEntity.getAssetCode(), integratedFutVolLiqEntity.getStartDate(),
                integratedFutVolLiqEntity.getFinishDate(), integratedFutVolLiqEntity.getAverageVolumeLiquidity(),
                integratedFutVolLiqEntity.getAverageValueLiquidity());
    }

}
