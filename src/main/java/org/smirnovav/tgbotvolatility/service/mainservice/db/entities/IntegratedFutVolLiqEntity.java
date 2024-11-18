package org.smirnovav.tgbotvolatility.service.mainservice.db.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "futures_volatility")
public class IntegratedFutVolLiqEntity {
    @Id
    @Column(name = "vol_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long volId;
    @Column(name = "ma_period")
    private int maPeriodInDays; // Период усреднения показателя в днях
    @Column(name = "sec_id")
    private String secId; // Краткий код биржевого инструмента
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "asset_code")
    private String assetCode; // База краткого наименования контракта
    @Column(name = "start_date")
    private Calendar startDate; // Начальная дата периода времени
    @Column(name = "finish_date")
    private Calendar finishDate; // Конечная дата периода времени
    @Column(name = "average_volume_liquidity")
    private BigDecimal averageVolumeLiquidity;
    @Column(name = "average_value_liquidity")
    private BigDecimal averageValueLiquidity;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "integratedFutVolLiq")
    private List<AtrValueEntity> values; // Значения показателя ATR в процентах

}
