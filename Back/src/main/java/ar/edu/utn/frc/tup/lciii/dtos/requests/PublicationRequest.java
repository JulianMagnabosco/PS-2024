package ar.edu.utn.frc.tup.lciii.dtos.requests;

import ar.edu.utn.frc.tup.lciii.dtos.SectionDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PublicationRequest {
    Long user;
    String name;
    String description;
    String type;
    String dificulty;
    String image;
    List<SectionDto> sections;
    boolean canSold;
    BigDecimal price;
    Long count;
}
