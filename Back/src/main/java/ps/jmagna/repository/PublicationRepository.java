package ps.jmagna.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ps.jmagna.entities.PublicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ps.jmagna.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity,Long>,
        JpaSpecificationExecutor<PublicationEntity> {
    List<PublicationEntity> findAllByDateTimeBetween(LocalDateTime date1, LocalDateTime date2);
    List<PublicationEntity> findAllByUserAndDraftIsTrueAndDeletedIsFalse(UserEntity user);

    @Query("SELECT p.name FROM PublicationEntity p WHERE LOWER( p.name ) LIKE %:keyword%")
    List<String> findNamesByNameContaining(@Param("keyword") String keyword, Pageable pageable);

}
