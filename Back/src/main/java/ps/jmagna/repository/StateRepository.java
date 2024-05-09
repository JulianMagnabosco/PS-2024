package ps.jmagna.repository;

import ps.jmagna.entities.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository  extends JpaRepository<StateEntity, Long> {
}
