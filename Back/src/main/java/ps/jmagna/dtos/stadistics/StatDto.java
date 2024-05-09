package ps.jmagna.dtos.stadistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    BigDecimal value;
    String name;

    public StatDto(String name){
        this.name = name;
        this.setValue(BigDecimal.ZERO);
    }
}
