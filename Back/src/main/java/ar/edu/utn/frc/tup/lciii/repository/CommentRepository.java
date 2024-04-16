package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findAllByPublication_Id(Long id);
    List<CommentEntity> findAllByFather_Id(Long id);
    List<CommentEntity> findAllByGrandfather_Id(Long id);
}
