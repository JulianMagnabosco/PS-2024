package ps.jmagna.dtos.publication;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PutPublicationRequest {
    Long id;
    String name;
    String description;
    String type;
    int difficulty;
    String video;
    List<SectionRequest> sections;
    boolean canSold;
    BigDecimal price;
    Long count;
}
