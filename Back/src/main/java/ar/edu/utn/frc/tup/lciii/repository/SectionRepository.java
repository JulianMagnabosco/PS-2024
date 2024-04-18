package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.SectionEntity;
import ar.edu.utn.frc.tup.lciii.enums.TypeSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity,Long> {
    List<SectionEntity> findAllByPublication(PublicationEntity publication);
    List<SectionEntity> findAllByPublicationAndType(PublicationEntity publication, TypeSec type);
    SectionEntity getByPublicationAndNumber(PublicationEntity publication, Long number);
    SectionEntity findFirstByPublicationAndType(PublicationEntity publication, TypeSec type);

    List<SectionEntity> deleteAllByPublication_Id(Long id);

}
