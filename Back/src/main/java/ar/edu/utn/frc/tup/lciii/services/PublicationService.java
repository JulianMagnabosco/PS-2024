package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.PublicationDto;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationMinDto;
import ar.edu.utn.frc.tup.lciii.dtos.SearchPubResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.CalificationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PutPublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.SearchPubRequest;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PublicationService {

    PublicationDto register(PublicationRequest request);
    boolean registerImg(MultipartFile[] images, String indexes) throws IOException;

    boolean calificate(CalificationRequest request);

    List<PublicationMinDto> getAll();

    SearchPubResponce getAllFilthered(SearchPubRequest searchPubRequest);

    PublicationDto get(Long id,Long userId);
    byte[] getImage(Long id);

    @Transactional
    PublicationDto put(PutPublicationRequest request);

    boolean delete(Long id);
}
