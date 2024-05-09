package ps.jmagna.entities;

import ps.jmagna.enums.SecType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    SecType type;
    @Column(columnDefinition="TEXT")
    String text;
    @Lob
    byte[] image;
    @ManyToOne
    @JoinColumn(name="idPublication")
    PublicationEntity publication;
}
