package ar.edu.utn.frc.tup.lciii.dtos.purchase;

import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.SaleDetailEntity;
import ar.edu.utn.frc.tup.lciii.entities.SaleEntity;
import ar.edu.utn.frc.tup.lciii.enums.SaleState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailDto {
    Long pubId;
    String name;
    String imageUrl;
    BigDecimal total;
    int count;
}
