package ps.jmagna.repository;

import ps.jmagna.entities.DeliveryEntity;
import ps.jmagna.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
    Long countAllByDealer(UserEntity dealer);

    List<DeliveryEntity> findAllByDealer_Id(Long dealer);
    List<DeliveryEntity> findAllByDealer(UserEntity dealer);
}
