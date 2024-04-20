package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity,Long>, JpaSpecificationExecutor<PublicationEntity> {
    List<PublicationEntity> findAllByCreationTimeBetween(LocalDateTime date1, LocalDateTime date2);

}
