package ps.jmagna.entities;

import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.Query;
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
    LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name="idUser")
    UserEntity user;
    String name;
    @Column(columnDefinition="TEXT")
    String description;
    @Enumerated(EnumType.STRING)
    PubType type;
    int difficulty;
    boolean canSold;
    BigDecimal price;
    Long count;
    String video;
    @Formula("(select round(coalesce(avg(h.points),0),2) from califications h where h.id_publication = id)")
    BigDecimal calification ;

    @OneToMany(mappedBy="publication")
    List<SectionEntity> sections;
    @OneToMany(mappedBy="publication")
    List<CalificationEntity> califications;
    @OneToMany(mappedBy="publication")
    List<SaleDetailEntity> saleDetails;
}
