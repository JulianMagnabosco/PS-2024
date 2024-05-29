package ps.jmagna.dtos.publication;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicationMinDto {
    Long id;
    String name;
    String description;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;
    String type;
    String difficulty;
    BigDecimal calification;
    String imageUrl;
    boolean canSold;
    BigDecimal price;
    Long count;
}
