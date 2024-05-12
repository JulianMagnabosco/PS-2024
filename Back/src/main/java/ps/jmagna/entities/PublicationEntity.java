package ps.jmagna.entities;

import ps.jmagna.enums.PubType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    boolean deleted = Boolean.FALSE;
    boolean draft = Boolean.FALSE;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationTime;
    @ManyToOne
    @JoinColumn(name="idUser")
    UserEntity user;
    String name;
    String description;
    @Enumerated(EnumType.STRING)
    PubType type;
    int difficulty;
    boolean canSold;
    BigDecimal price;
    Long count;
    @OneToMany(mappedBy="publication")
    List<SectionEntity> sections;
    @OneToMany(mappedBy="publication")
    List<CalificationEntity> califications;
    @OneToMany(mappedBy="publication")
    List<SaleDetailEntity> saleDetails;
}
