package ps.jmagna.dtos.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailDto {
    Long pubId;
    String name;
    String imageUrl;
    BigDecimal total;
    int count;
}
