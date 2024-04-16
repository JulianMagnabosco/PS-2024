package ar.edu.utn.frc.tup.lciii.dtos.requests;

import lombok.Data;

@Data
public class CommentRequest {
    Long user;
    Long pub;
    Long father;
    String text;
}
