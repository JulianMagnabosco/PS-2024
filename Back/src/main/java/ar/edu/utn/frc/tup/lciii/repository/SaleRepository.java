package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity,Long> {
    Optional<SaleEntity> findByMerchantOrder(Long merchantOrder);
    List<SaleEntity> findAllByDateTimeBetween(LocalDateTime first,LocalDateTime last);
}
