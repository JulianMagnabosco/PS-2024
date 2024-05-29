package ps.jmagna.dtos.publication;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {
    Long id;
    Long pub;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;

    Long userId;
    String username;
    String userIconUrl;

    String text;

    Long idFather;
    String fatherText;

    List<CommentDto> children;

    boolean deleted;
}
