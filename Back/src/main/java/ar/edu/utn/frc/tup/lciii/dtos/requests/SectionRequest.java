package ar.edu.utn.frc.tup.lciii.dtos.requests;

import ar.edu.utn.frc.tup.lciii.enums.SecType;
import lombok.Data;


@Data
public class SectionRequest {
    Long number;
    SecType type;
    String text;
}
