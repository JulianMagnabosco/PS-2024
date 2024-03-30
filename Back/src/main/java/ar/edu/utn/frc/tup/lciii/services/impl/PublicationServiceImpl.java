package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.PublicationRequest;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.SectionEntity;
import ar.edu.utn.frc.tup.lciii.repository.PublicationRepository;
import ar.edu.utn.frc.tup.lciii.repository.SectionRepository;
import ar.edu.utn.frc.tup.lciii.services.PublicationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
}
