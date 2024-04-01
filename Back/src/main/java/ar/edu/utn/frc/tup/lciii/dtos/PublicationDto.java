package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicationDto {
    Long id;
    String name;
    String description;
    String type;
    String dificulty;
    String image;
    List<String> conditions;
    List<String> materials;
    List<String> steps;
    List<String> stepPhothos;
    boolean canSold;
    BigDecimal price;

}
