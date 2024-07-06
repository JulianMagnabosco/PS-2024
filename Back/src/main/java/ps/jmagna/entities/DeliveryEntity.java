package ps.jmagna.entities;

import ps.jmagna.enums.DeliveryState;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "deliveries")
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    Long shipmment;
    @ManyToOne
    @JoinColumn(name="idSale")
    SaleEntity sale;
    @ManyToOne
    @JoinColumn(name="idDealer")
    UserEntity dealer;
    @Enumerated(EnumType.STRING)
    DeliveryState deliveryState;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;
}
