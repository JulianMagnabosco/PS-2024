package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.FilterDTO;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationDto;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationMinDto;
import ar.edu.utn.frc.tup.lciii.dtos.SearchResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.SearchRequest;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;

import java.util.List;

public interface PublicationService {

    PublicationEntity register(PublicationRequest request);
    List<PublicationMinDto> getAll();

    SearchResponce getAllFilthered(SearchRequest searchRequest);

    PublicationDto get(Long id);
}
