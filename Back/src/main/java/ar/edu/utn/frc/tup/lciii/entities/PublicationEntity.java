package ar.edu.utn.frc.tup.lciii.entities;

import ar.edu.utn.frc.tup.lciii.enums.Difficulty;
import ar.edu.utn.frc.tup.lciii.enums.TypePub;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.boot.context.properties.bind.DefaultValue;

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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationTime;
    @ManyToOne
    @JoinColumn(name="idUser")
    UserEntity user;
    String name;
    String description;
    @Enumerated(EnumType.STRING)
    TypePub type;
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
