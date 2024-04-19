package ar.edu.utn.frc.tup.lciii.dtos.stadistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatSeriesDto {
    String name;
    List<StatDto> series;
}
