package ps.jmagna.dtos.publication;

import lombok.Data;

@Data
public class CommentRequest {
    Long user;
    Long pub;
    Long father;
    Long grandfather;
    String text;
}
