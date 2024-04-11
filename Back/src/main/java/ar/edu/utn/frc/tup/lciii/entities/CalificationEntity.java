package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "califications")
@AllArgsConstructor
@NoArgsConstructor
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
    BigDecimal points;
}
