package ps.jmagna.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    boolean deleted = Boolean.FALSE;
    String text;
    @ManyToOne
    @JoinColumn(name="idUser")
    UserEntity user;
    @ManyToOne
    @JoinColumn(name="idFather")
    CommentEntity father;
    @ManyToOne
    @JoinColumn(name="idGrandfather")
    CommentEntity grandfather;
    @ManyToOne
    @JoinColumn(name="idPublication")
    PublicationEntity publication;
    @OneToMany(mappedBy="father")
    List<CommentEntity> childs;
}
