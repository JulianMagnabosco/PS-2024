package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.*;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.SearchRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicationServiceImpl implements PublicationService {
    @Autowired
    PublicationRepository pRepository;
    @Autowired
    SectionRepository sRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    @Transactional
    public PublicationEntity register(PublicationRequest request) {

        PublicationEntity publication = modelMapper.map(request, PublicationEntity.class);
        pRepository.save(publication);

//        List<SectionEntity> sectionEntities = new ArrayList<>();
        for (SectionDto sectionDto: request.getSections()) {
            SectionEntity s = modelMapper.map(sectionDto, SectionEntity.class);
            s.setPublication(publication);
//            sectionEntities.add(s);
            sRepository.save(s);
        }

        return publication;
    }


    //Listar
    @Override
    public List<PublicationMinDto> getAll() {
        List<PublicationEntity> list = pRepository.findAll();
        return modelMapper.map(list,new TypeToken<List<PublicationMinDto>>() {}.getType());
    }

    @Override
    public SearchResponce getAllFilthered(SearchRequest searchRequest) {
        SearchResponce responce = new SearchResponce();
        if(searchRequest.getPage() < 1) searchRequest.setPage(1);
        if(searchRequest.getSize() < 10) searchRequest.setSize(10);

        //The third Sort parameter is optional
        Pageable pageable = PageRequest.of(searchRequest.getPage()-1, searchRequest.getSize());

        Page<PublicationEntity> all = pRepository.findAll(createFilter(searchRequest),pageable);
        List<PublicationMinDto> list=new ArrayList<>();
        for (PublicationEntity p: all
             ) {
            list.add(modelMapper.map(p, PublicationMinDto.class));
        }
        responce.setList(list);

        responce.setCountTotal(pRepository.count(createFilter(searchRequest)));

        return responce;
    }

    //Filtros
    public static Specification<PublicationEntity> createFilter(SearchRequest searchRequest)
    {
        return new Specification<PublicationEntity>()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<PublicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
            {
                List<Predicate> predicates = new ArrayList<>();
                if(searchRequest.getType()!= TypePub.TODO)
                {
                    Predicate predicate = criteriaBuilder.equal(root.get("type"),searchRequest.getType().toString());
                    predicates.add(predicate);
                };
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }; //columnEqual() function ends
    }


    @Override
    public PublicationDto get(Long id) throws EntityNotFoundException {
        PublicationDto responce;
        PublicationEntity p = pRepository.getReferenceById(id);
        if(p==null){
            throw new EntityNotFoundException();
        }
        responce = modelMapper.map(p,PublicationDto.class);
        responce.setSections(new ArrayList<>());
        for (SectionEntity s : sRepository.findAllByPublication(p)) {
            responce.getSections().add(modelMapper.map(s,SectionDto.class));
        }

        return responce;
    }
}
