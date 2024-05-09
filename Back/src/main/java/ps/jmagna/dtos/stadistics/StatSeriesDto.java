package ps.jmagna.dtos.stadistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatSeriesDto {
    String name;
    List<StatDto> series;
}
