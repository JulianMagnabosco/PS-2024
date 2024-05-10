package ps.jmagna.repository;

import ps.jmagna.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.jmagna.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity,Long> {
    Optional<SaleEntity> findByMerchantOrder(Long merchantOrder);
//    List<SaleEntity> findAllByDateTimeBetween(LocalDateTime first,LocalDateTime last);
    List<SaleEntity> findAllByDateTimeBetweenAndUser_Id(LocalDateTime first, LocalDateTime last, Long user);
    List<SaleEntity> findAllByDateTimeBetweenAndUser(LocalDateTime first, LocalDateTime last, UserEntity user);
    List<SaleEntity> findAllByDateTimeBetween(LocalDateTime first, LocalDateTime last);
}
