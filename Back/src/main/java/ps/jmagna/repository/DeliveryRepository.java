package ps.jmagna.repository;

import ps.jmagna.entities.DeliveryEntity;
import ps.jmagna.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import ps.jmagna.enums.DeliveryState;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
    Long countAllByDealer(UserEntity dealer);

    List<DeliveryEntity> findAllByDealer_Id(Long dealer);
    List<DeliveryEntity> findAllByDealer(UserEntity dealer);
    List<DeliveryEntity> findAllByDeliveryState(DeliveryState state);
    List<DeliveryEntity> findAllByDeliveryStateAndDealer(DeliveryState state, UserEntity dealer);
}
