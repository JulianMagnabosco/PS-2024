package ps.jmagna.dtos.purchase;

import ps.jmagna.enums.DeliveryState;
import lombok.Data;

@Data
public class PutDeliveryRequest {
    Long id;
    Long dealer;
    DeliveryState deliveryState;
}
