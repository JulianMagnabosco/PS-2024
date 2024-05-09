package ps.jmagna.repository;

import ps.jmagna.entities.CalificationEntity;
import ps.jmagna.entities.PublicationEntity;
import ps.jmagna.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificationRepository extends JpaRepository<CalificationEntity, Long> {
    Optional<CalificationEntity> getByUserAndPublication(UserEntity user, PublicationEntity publication);
    List<CalificationEntity> findAllByPublication(PublicationEntity publication);
}
