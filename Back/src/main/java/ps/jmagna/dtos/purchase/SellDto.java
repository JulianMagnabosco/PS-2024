package ps.jmagna.dtos.purchase;

import ps.jmagna.enums.SaleState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellDto {
    Long id;
    String dateTime;
    SaleState saleState;
    Long pubId;
    String buyer;
    String name;
    String imageUrl;
    BigDecimal total;
    int count;
}
