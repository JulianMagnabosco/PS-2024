package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.FilterDTO;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationDto;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationMinDto;
import ar.edu.utn.frc.tup.lciii.dtos.SearchResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.CalificationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.SearchRequest;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PublicationService {

    PublicationDto register(PublicationRequest request);
    boolean registerImg(MultipartFile[] images, String indexes) throws IOException;

    boolean calificate(CalificationRequest request);

    List<PublicationMinDto> getAll();

    SearchResponce getAllFilthered(SearchRequest searchRequest);

    PublicationDto get(Long id);
    byte[] getImage(Long id);
}
