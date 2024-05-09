package ps.jmagna.dtos.publication;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ListCommentsResponce {
    int countTotal;
    List<CommentDto> list;
}
