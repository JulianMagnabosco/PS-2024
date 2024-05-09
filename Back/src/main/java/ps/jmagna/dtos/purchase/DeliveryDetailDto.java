package ps.jmagna.dtos.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDetailDto {
    Long pubId;
    String name;
    String imageUrl;
    BigDecimal total;
    int count;

    String sellername;
    String phone;
    String direction;
}
