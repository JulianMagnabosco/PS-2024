package ar.edu.utn.frc.tup.lciii.entities;

import ar.edu.utn.frc.tup.lciii.enums.Difficulty;
import ar.edu.utn.frc.tup.lciii.enums.TypePub;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "publications")
@AllArgsConstructor
@NoArgsConstructor
public class PublicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name="idUser")
    UserEntity user;
    String name;
    String description;
    @Enumerated(EnumType.STRING)
    TypePub type;
    @Enumerated(EnumType.STRING)
    Difficulty difficulty;
    boolean canSold;
    BigDecimal price;
    Long count;
    @OneToMany(mappedBy="publication")
    List<SectionEntity> sections;
}
