package ps.jmagna.repository;

import ps.jmagna.entities.UserEntity;
import ps.jmagna.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByDateTimeBetween(LocalDateTime date1, LocalDateTime date2);
    List<UserEntity> findAllByRole(UserRole role);

    UserEntity getByUsername(String username);
    UserEntity getByEmail(String email);
    UserDetails findByUsername(String username);
    UserDetails findByEmail(String email);
}
