package ar.edu.utn.frc.tup.lciii.dtos;

import ar.edu.utn.frc.tup.lciii.enums.TypeSec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SectionDto {
    Long number;
    TypeSec type;
    String text;
    String image;
}
