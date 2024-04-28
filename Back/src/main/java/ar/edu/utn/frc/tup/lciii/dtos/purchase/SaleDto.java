package ar.edu.utn.frc.tup.lciii.dtos.purchase;

import ar.edu.utn.frc.tup.lciii.enums.SaleState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto {
    Long id;
    String dateTime;
    List<SaleDetailDto> details;
    SaleState saleState;

}
