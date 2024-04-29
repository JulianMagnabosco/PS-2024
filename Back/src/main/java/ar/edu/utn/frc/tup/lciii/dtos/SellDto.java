package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellDto {
    Long pubId;
    String buyer;
    String name;
    String imageUrl;
    BigDecimal total;
    int count;
}
