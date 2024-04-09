package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.*;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.SearchRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.SectionRequest;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.SectionEntity;
import ar.edu.utn.frc.tup.lciii.enums.TypePub;
import ar.edu.utn.frc.tup.lciii.enums.TypeSec;
import ar.edu.utn.frc.tup.lciii.repository.PublicationRepository;
import ar.edu.utn.frc.tup.lciii.repository.SectionRepository;
import ar.edu.utn.frc.tup.lciii.services.PublicationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class PublicationServiceImpl implements PublicationService {
    @Autowired
    PublicationRepository pRepository;
    @Autowired
    SectionRepository sRepository;
    @Autowired
    ModelMapper modelMapper;

    @Value("${app.url}")
    private String url;

    @Override
    @Transactional
    public PublicationDto register(PublicationRequest request) {

        PublicationEntity publication = modelMapper.map(request, PublicationEntity.class);
        pRepository.save(publication);

        List<SectionEntity> sectionEntities = new ArrayList<>();
        for (SectionRequest sectionDto : request.getSections()) {
            SectionEntity s = modelMapper.map(sectionDto, SectionEntity.class);
            s.setPublication(publication);

            sectionEntities.add(sRepository.save(s));
        }
        publication.setSections(sectionEntities);

        return get(publication.getId());
    }

    @Override
    @Transactional
    public boolean registerImg(MultipartFile[] images, String indexes) throws IOException {

        int i=0;
        for (String c : indexes.split("_")) {
            Long id = Long.parseLong(c);
            SectionEntity s = sRepository.getReferenceById(id);
            s.setImage(compressBytes(images[i].getBytes()));
            sRepository.save(s);
            i++;
        }

        return true;
    }

    //Listar
    @Override
    public List<PublicationMinDto> getAll() {
        List<PublicationEntity> list = pRepository.findAll();
        return modelMapper.map(list, new TypeToken<List<PublicationMinDto>>() {
        }.getType());
    }

    @Override
    public SearchResponce getAllFilthered(SearchRequest searchRequest) {
        SearchResponce responce = new SearchResponce();
        if (searchRequest.getPage() < 1) searchRequest.setPage(1);
        if (searchRequest.getSize() < 10) searchRequest.setSize(10);

        //The third Sort parameter is optional
        Pageable pageable = PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize());

        Page<PublicationEntity> all = pRepository.findAll(createFilter(searchRequest), pageable);
        List<PublicationMinDto> list = new ArrayList<>();
        for (PublicationEntity p : all) {
            PublicationMinDto dto = modelMapper.map(p, PublicationMinDto.class);

            SectionEntity sectionImage = sRepository.findFirstByPublicationAndType(p,TypeSec.PHOTO);
            if (sectionImage != null) {
                dto.setImageUrl(url + "/pub/image/" + sectionImage.getId());
            }

            list.add(dto);
        }
        responce.setList(list);

        responce.setCountTotal(pRepository.count(createFilter(searchRequest)));

        return responce;
    }

    //Filtros
    public static Specification<PublicationEntity> createFilter(SearchRequest searchRequest) {
        return new Specification<PublicationEntity>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<PublicationEntity> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (searchRequest.getType() != TypePub.NONE) {
                    Predicate predicate = criteriaBuilder.equal(root.get("type"), searchRequest.getType().toString());
                    predicates.add(predicate);
                }
                ;
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }; //columnEqual() function ends
    }


    @Override
    public PublicationDto get(Long id) throws EntityNotFoundException {
        PublicationDto responce;
        PublicationEntity p = pRepository.getReferenceById(id);
        if (p == null) {
            throw new EntityNotFoundException();
        }

        responce = modelMapper.map(p, PublicationDto.class);

        List<SectionDto> sections = new ArrayList<>();
        for (SectionEntity s : sRepository.findAllByPublication(p)) {
            SectionDto r = modelMapper.map(s, SectionDto.class);
            if (s.getImage() != null) {
                r.setImageUrl(url + "/pub/image/" + r.getId());
            }
            sections.add(r);
        }
        responce.setSections(sections);

        return responce;
    }

    @Override
    public byte[] getImage(Long id) {

        SectionEntity s = sRepository.getReferenceById(id);
        if (s == null) {
            throw new EntityNotFoundException();
        }

        return decompressBytes(s.getImage());
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
//        System.out.println("From" + data.length);
//        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
