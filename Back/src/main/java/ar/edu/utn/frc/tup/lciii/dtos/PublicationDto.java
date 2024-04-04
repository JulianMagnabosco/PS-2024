package ar.edu.utn.frc.tup.lciii.dtos;

import ar.edu.utn.frc.tup.lciii.enums.Difficulty;
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
    Difficulty difficulty;
    String image;
    List<SectionDto> sections;
    boolean canSold;
    BigDecimal price;
    Long count;

}
