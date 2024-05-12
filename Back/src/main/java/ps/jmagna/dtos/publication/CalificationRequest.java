package ps.jmagna.dtos.publication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalificationRequest {
    Long pubId;
    BigDecimal value;
}
