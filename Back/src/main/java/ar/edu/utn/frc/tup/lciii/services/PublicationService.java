package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.FilterDTO;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationDto;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationMinDto;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;

import java.util.List;

public interface PublicationService {

    PublicationEntity register(PublicationRequest request);
    List<PublicationMinDto> getAll();

    List<PublicationMinDto> getAllFilthered(List<FilterDTO> filterDTOList, int page, int size);

    PublicationDto get(Long id);
}
