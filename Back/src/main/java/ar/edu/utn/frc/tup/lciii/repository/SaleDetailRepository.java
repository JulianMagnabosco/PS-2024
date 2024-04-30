package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.SaleDetailEntity;
import ar.edu.utn.frc.tup.lciii.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleDetailRepository  extends JpaRepository<SaleDetailEntity,Long> {
    List<SaleDetailEntity> findAllBySale_DateTimeBetweenAndPublication_User_Id(LocalDateTime first,
                                                                          LocalDateTime last,
                                                                          Long user);
}
