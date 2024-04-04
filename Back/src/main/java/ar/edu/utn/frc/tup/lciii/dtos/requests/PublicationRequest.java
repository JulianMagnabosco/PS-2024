package ar.edu.utn.frc.tup.lciii.dtos.requests;

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
    List<String> conditions;
    List<String> materials;
    List<String> steps;
    boolean canSold;
    BigDecimal price;
    Long count;
}
