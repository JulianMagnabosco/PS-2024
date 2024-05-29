package ps.jmagna.repository;

import org.springdoc.core.converters.models.Sort;
import ps.jmagna.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findAllByPublication_Id(Long id);
    List<CommentEntity> findAllByGrandfather_Id(Long id);
    List<CommentEntity> findAllByPublication_IdAndGrandfatherIsNull(Long id);
    List<CommentEntity> findAllByPublication_IdAndGrandfatherIsNullOrderByDateTime(Long id);
    List<CommentEntity> findAllByGrandfather_IdAndGrandfatherIsNullOrderByDateTime(Long id);
}
