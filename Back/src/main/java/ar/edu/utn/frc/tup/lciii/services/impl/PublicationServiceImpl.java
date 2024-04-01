package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.FilterDTO;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationDto;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationMinDto;
import ar.edu.utn.frc.tup.lciii.dtos.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.SectionEntity;
import ar.edu.utn.frc.tup.lciii.repository.PublicationRepository;
import ar.edu.utn.frc.tup.lciii.repository.SectionRepository;
import ar.edu.utn.frc.tup.lciii.services.PublicationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PublicationEntity register(PublicationRequest request) {

        PublicationEntity publication = modelMapper.map(request, PublicationEntity.class);
        pRepository.save(publication);

        List<SectionEntity> conditions = new ArrayList<>();
        for (String cond: request.getConditions()) {
            SectionEntity s = new SectionEntity();
            s.setText(cond);
            s.setType("CONDITION");
            s.setPublication(publication);
            conditions.add(s);
            sRepository.save(s);
        }

        List<SectionEntity> materials = new ArrayList<>();
        for (String cond: request.getConditions()) {
            SectionEntity s = new SectionEntity();
            s.setText(cond);
            s.setType("MATERIAL");
            s.setPublication(publication);
            materials.add(s);
            sRepository.save(s);
        }

        return publication;
    }


    //Listar
    @Override
    public List<PublicationMinDto> getAll() {
        List<PublicationEntity> list = pRepository.findAll(Sort.by(Sort.Direction.DESC,"ty"));
        return modelMapper.map(list,new TypeToken<List<PublicationMinDto>>() {}.getType());
    }

    @Override
    public List<PublicationMinDto> getAllFilthered(List<FilterDTO> filterDTOList, int page, int size) { if(page < 1)
        page = 1;

        if(size < 1)
            size = 10;

        //The third Sort parameter is optional
        Pageable pageable = PageRequest.of(page-1, size,Sort.by("id").descending());

        Page<PublicationEntity> list = pRepository.findAll(columnEqual(filterDTOList),pageable);
        return modelMapper.map(list,new TypeToken<List<PublicationMinDto>>() {}.getType());
    }

    //Filtros
    public static Specification<PublicationEntity> columnEqual(List<FilterDTO> filterDTOList)
    {
        return new Specification<PublicationEntity>()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<PublicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
            {
                List<Predicate> predicates = new ArrayList<>();
                filterDTOList.forEach(filter ->
                {
                    Predicate predicate = criteriaBuilder.equal(root.get(filter.getColumnName()),filter.getColumnValue());
                    predicates.add(predicate);
                });
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }; //columnEqual() function ends
    }


    @Override
    public PublicationDto get(Long id) throws EntityNotFoundException {
        PublicationEntity p = pRepository.getReferenceById(id);
        if(p==null){
            throw new EntityNotFoundException();
        }
        return modelMapper.map(p,PublicationDto.class);
    }
}
