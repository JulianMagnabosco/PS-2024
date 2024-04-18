package ar.edu.utn.frc.tup.lciii.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PutPublicationRequest {
    Long id;
    Long user;
    String name;
    String description;
    String type;
    int difficulty;
    String video;
    List<SectionRequest> sections;
    boolean canSold;
    BigDecimal price;
    Long count;
}
