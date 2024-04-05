package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicationMinDto {
    Long id;
    String name;
    String description;
    String type;
    String dificulty;
    byte[] image;
    boolean canSold;
    BigDecimal price;
}
