package ar.edu.utn.frc.tup.lciii.entities;

import ar.edu.utn.frc.tup.lciii.enums.TypeSec;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "sections")
@AllArgsConstructor
@NoArgsConstructor
public class SectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long number;
    @Enumerated(EnumType.STRING)
    TypeSec type;
    @Column(columnDefinition="TEXT")
    String text;
    @Column(nullable = true)
    String image;
    @ManyToOne
    @JoinColumn(name="idPublication")
    PublicationEntity publication;
}
