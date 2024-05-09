package ps.jmagna.repository;

import ps.jmagna.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findAllByPublication_Id(Long id);
    List<CommentEntity> findAllByFather_Id(Long id);
    List<CommentEntity> findAllByGrandfather_Id(Long id);
}
