package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.DeliveryEntity;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
    Long countAllByDealer(UserEntity dealer);

    List<DeliveryEntity> findAllByDealer_Id(Long dealer);
}
