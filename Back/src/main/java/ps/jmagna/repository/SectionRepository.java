package ps.jmagna.repository;

import ps.jmagna.entities.PublicationEntity;
import ps.jmagna.entities.SectionEntity;
import ps.jmagna.enums.SecType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity,Long> {
    List<SectionEntity> findAllByPublication(PublicationEntity publication);
    List<SectionEntity> findAllByPublication_IdAndType(Long id, SecType secType);
    SectionEntity findFirstByPublicationAndType(PublicationEntity publication, SecType type);
    List<SectionEntity> deleteAllByPublication_Id(Long id);

}
