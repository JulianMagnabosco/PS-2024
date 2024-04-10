package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.CalificationEntity;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificationRepository extends JpaRepository<CalificationEntity, Long> {
    Optional<CalificationEntity> getByUserAndPublication(UserEntity user, PublicationEntity publication);
    List<CalificationEntity> findAllByPublication(PublicationEntity publication);
}
