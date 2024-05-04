package ar.edu.utn.frc.tup.lciii.dtos.requests;

import ar.edu.utn.frc.tup.lciii.enums.DeliveryState;
import lombok.Data;

@Data
public class PutDeliveryRequest {
    Long id;
    Long dealer;
    DeliveryState deliveryState;
}
