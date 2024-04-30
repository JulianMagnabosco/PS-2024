package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.SectionEntity;
import ar.edu.utn.frc.tup.lciii.enums.SecType;
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
