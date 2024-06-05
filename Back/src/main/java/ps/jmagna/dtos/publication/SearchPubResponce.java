package ps.jmagna.dtos.publication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPubResponce {
    Long countTotal;
    List<PublicationMinDto> list;
}
