package ps.jmagna.dtos.publication;

import ps.jmagna.enums.PubType;
import lombok.Data;
import ps.jmagna.enums.SortType;

import java.math.BigDecimal;

@Data
public class SearchPubRequest {
    String text;
    String materials;
    PubType type;
    int diffMin;
    int diffMax;
    BigDecimal points;
    boolean mine;
    SortType sort;
    int page;
    int size;
}
