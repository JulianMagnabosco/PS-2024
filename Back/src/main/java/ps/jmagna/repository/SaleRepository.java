package ps.jmagna.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<SaleEntity> findAllByDateTimeBetweenAndUser(LocalDateTime dateTimeStart,
                                                     LocalDateTime dateTimeEnd,
                                                     UserEntity user,
                                                     Sort sort);
    List<SaleEntity> findAllByDateTimeBetween(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);
}
