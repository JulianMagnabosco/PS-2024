package ps.jmagna.dtos.publication;

import ps.jmagna.enums.SecType;
import lombok.Data;


@Data
public class SectionRequest {
    Long number;
    SecType type;
    String text;
}
