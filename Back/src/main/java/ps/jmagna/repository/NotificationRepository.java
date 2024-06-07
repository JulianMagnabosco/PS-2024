package ps.jmagna.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.jmagna.entities.NotificationEntity;
import ps.jmagna.entities.UserEntity;

import javax.swing.text.html.parser.Entity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findAllByDeletedIsFalseAndUser(UserEntity user, Pageable pageable);
}
