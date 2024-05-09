package ps.jmagna.dtos.publication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPubResponce {
    int countTotal;
    List<PublicationMinDto> list;
}
