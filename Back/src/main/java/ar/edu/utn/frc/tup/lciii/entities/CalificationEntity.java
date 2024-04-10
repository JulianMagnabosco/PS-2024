package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "publications")
public class CalificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name="idUser")
    UserEntity user;
    @ManyToOne
    @JoinColumn(name="idPublication")
    PublicationEntity publication;
    BigDecimal value;
}
