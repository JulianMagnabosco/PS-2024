package ps.jmagna.dtos.publication;

import ps.jmagna.enums.SecType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SectionDto {
    Long id;
    Long number;
    SecType type;
    String text;
    String imageUrl;
}
