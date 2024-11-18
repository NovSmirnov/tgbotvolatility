package org.smirnovav.tgbotvolatility.service.mainservice.db.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "atr_values")
public class AtrValueEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "value")
    private double value;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "vol_id", nullable = false)
    private IntegratedFutVolLiqEntity integratedFutVolLiq;
    @Column(name = "value_number")
    private int valueNumber;
}
