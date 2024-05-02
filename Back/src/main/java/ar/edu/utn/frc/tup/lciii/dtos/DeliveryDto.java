package ar.edu.utn.frc.tup.lciii.dtos;

import ar.edu.utn.frc.tup.lciii.enums.DeliveryState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {
    Long id;
    String saleDateTime;
    List<DeliveryDetailDto> details;
    BigDecimal total;

    String name;
    String phone;
    String direction;

    String deliveryDateTime;
    DeliveryState deliveryState;

    String dealer;
    Long dealerId;
}
