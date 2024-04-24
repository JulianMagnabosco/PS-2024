package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "saleDetails")
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name="idSale")
    SaleEntity sale;

    @ManyToOne
    @JoinColumn(name="idPublication")
    PublicationEntity publication;
    BigDecimal total;
    int count;
}
