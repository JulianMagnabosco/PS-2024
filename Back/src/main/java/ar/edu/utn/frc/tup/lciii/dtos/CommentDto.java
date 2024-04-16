package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {
    Long pub;
    Long userId;
    String username;
    String userIconUrl;
    String text;
    List<CommentDto> childs;
}
