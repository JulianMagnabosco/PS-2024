package ps.jmagna.entities;

import ps.jmagna.enums.SaleState;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "sales")
@AllArgsConstructor
@NoArgsConstructor
public class SaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    Long merchantOrder;
    @ManyToOne
    @JoinColumn(name="idUser")
    UserEntity user;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;
    @OneToMany(mappedBy="sale", cascade = CascadeType.ALL)
    List<SaleDetailEntity> details;
    @Enumerated(EnumType.STRING)
    SaleState saleState;
}
