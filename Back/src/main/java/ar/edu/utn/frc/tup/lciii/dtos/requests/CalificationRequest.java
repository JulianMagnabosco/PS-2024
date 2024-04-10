package ar.edu.utn.frc.tup.lciii.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalificationRequest {
    Long userId;
    Long pubId;
    BigDecimal value;
}
