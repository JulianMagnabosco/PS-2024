package ps.jmagna.repository;

import ps.jmagna.entities.SaleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import ps.jmagna.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleDetailRepository  extends JpaRepository<SaleDetailEntity,Long> {
    List<SaleDetailEntity> findAllBySale_DateTimeBetweenAndPublication_User_Id(LocalDateTime first,
                                                                               LocalDateTime last,
                                                                               Long user);
    List<SaleDetailEntity> findAllBySale_DateTimeBetweenAndPublication_User(LocalDateTime first,
                                                                               LocalDateTime last,
                                                                               UserEntity user);
}
