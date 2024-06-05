package ps.jmagna.services;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ps.jmagna.dtos.publication.*;
import ps.jmagna.entities.*;
import ps.jmagna.enums.Difficulty;
import ps.jmagna.enums.PubType;
import ps.jmagna.enums.SecType;
import ps.jmagna.enums.SortType;
import ps.jmagna.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.commons.compress.utils.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class PublicationService {
    @Autowired
    PublicationRepository publicationRepository;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CalificationRepository calificationRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ModelMapper modelMapper;

    @Value("${app.url}")
    private String url;

    @Transactional
    public PublicationDto register(PublicationRequest request, UserEntity user) {

        PublicationEntity publication = modelMapper.map(request, PublicationEntity.class);
        publication.setUser(user);
        publication.setDateTime(LocalDateTime.now());
        publicationRepository.save(publication);

        List<SectionEntity> sectionEntities = new ArrayList<>();
        for (SectionRequest sectionDto : request.getSections()) {
            SectionEntity s = modelMapper.map(sectionDto, SectionEntity.class);
            s.setPublication(publication);

            sectionEntities.add(sectionRepository.save(s));
        }
        publication.setSections(sectionEntities);


        publicationRepository.saveAndFlush(publication);

        return get(publication.getId(), user);
    }

    @Transactional
    public boolean registerImg(MultipartFile[] images, String indexes, UserEntity user) throws IOException {
        
        int i = 0;
        for (String c : indexes.split("_")) {
            Long id = Long.parseLong(c);
            SectionEntity s = sectionRepository.getReferenceById(id);
            if(user.getId() != s.getPublication().getUser().getId()) {
                throw new IllegalArgumentException("Usuario incorrecto");
            }
            s.setImage(compressBytes(images[i].getBytes()));
            sectionRepository.save(s);
            i++;
        }

        return true;
    }

    public boolean calificate(CalificationRequest request, UserEntity user) {
        
        PublicationEntity p = publicationRepository.getReferenceById(request.getPubId());
        Optional<CalificationEntity> calificationAsk = calificationRepository.getByUserAndPublication(user, p);
        CalificationEntity calification;
        if (calificationAsk.isEmpty()) {
            calification = new CalificationEntity();
            calification.setPublication(p);
            calification.setUser(user);
        } else {
            calification = calificationAsk.get();
        }

        calification.setPoints(request.getValue());
        calificationRepository.save(calification);

        return true;
    }

    public boolean addCart(CalificationRequest request, UserEntity user) {
        
        PublicationEntity p = publicationRepository.getReferenceById(request.getPubId());
        Optional<CartEntity> cartAsk = cartRepository.getByUserAndPublication(user, p);

        int value = request.getValue().intValue();
        CartEntity cart;
        if (cartAsk.isEmpty()) {
            if (value <= 0) return false;
            cart = new CartEntity();
            cart.setPublication(p);
            cart.setUser(user);
        } else {
            if (value <= 0) {
                cartRepository.deleteById(cartAsk.get().getId());
                return true;
            }
            cart = cartAsk.get();
        }
        cart.setCount(value);
        cartRepository.save(cart);


        return true;
    }

    //Listar

    public SearchPubResponce getAll(SearchPubRequest request, UserEntity user) {

        SearchPubResponce responce = new SearchPubResponce();

        Pageable pageable;
        if(request.getSort().equals(SortType.CALF)){
//            list.sort(Comparator.comparing(PublicationMinDto::getCalification).reversed());
            pageable = PageRequest.of(request.getPage(), request.getSize(),
                    Sort.by("calification").descending());
        }
        else {
            pageable = PageRequest.of(request.getPage(), request.getSize(),
                    Sort.by("dateTime"));
        }
        Page<PublicationEntity> all = publicationRepository.findAll(createFilter(request, user), pageable);

//        int count = (int) all.getTotalElements();
        List<PublicationMinDto> list = new ArrayList<>();
        for (PublicationEntity p : all) {

//

            PublicationMinDto dto = modelMapper.map(p, PublicationMinDto.class);

//            dto.setCalification(cal);
            dto.setDifficulty(Difficulty.values()[p.getDifficulty()].name());
            SectionEntity sectionImage = sectionRepository.findFirstByPublicationAndType(p, SecType.PHOTO);
            if (sectionImage != null) {
                dto.setImageUrl(url + "/api/image/pub/" + sectionImage.getId());
            }
            list.add(dto);
        }

        responce.setList(list);

        responce.setCountTotal(all.getTotalElements());

        return responce;
    }

    public static Specification<PublicationEntity> createFilter(SearchPubRequest request, UserEntity user) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
            predicates.add(criteriaBuilder.isFalse(root.get("draft")));

            Predicate difficulty = criteriaBuilder.between(
                    root.get("difficulty"),
                    request.getDiffMin(),
                    request.getDiffMax());
            predicates.add(difficulty);

            if (request.getType() != PubType.NONE) {
                predicates.add(criteriaBuilder.equal(root.get("type"), request.getType()));
            }

            if (request.isMine()){
                predicates.add(criteriaBuilder.notEqual(root.get("user"), user));
            }

            if (!request.getText().isBlank()) {
                List<String> texts = List.of(request.getText().toLowerCase()
                        .split("\\s"));
                List<String> users= new ArrayList<>();
                for (String t : texts) {
                    if (t.charAt(0) == '@'){
                        users.add(t.substring(1));
                    }
                    else if (t.charAt(0) == '#') {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description"))
                                , "%" + t + "%"));
                    }
                    else {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name"))
                                , "%" + t + "%"));
                    }
                }

                if(!users.isEmpty()){
                    Join<PublicationEntity, UserEntity> join = root.join("user");
                    predicates.add(join.get("username").in(users));
                }
            }

            if (request.getPoints().compareTo(BigDecimal.ZERO)==1){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("calification"), request.getPoints()));
            }


            if (!request.getMaterials().isBlank()) {

                List<String> mats = List.of(request.getMaterials().toLowerCase()
                        .split("[\\s,]+"));
                for (String m : mats) {
                    Join<PublicationEntity, SectionEntity> join = root.join("sections", JoinType.INNER);
                    predicates.add(criteriaBuilder.like(join.get("text"), m));
                }
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }; //columnEqual() function ends
    }
    public List<String> getSuggestions(String text){

        Pageable topFive = PageRequest.of(0, 5);
        return publicationRepository.findNamesByNameContaining(text.toLowerCase(),topFive);
    }
    public List<CartDto> getCart(UserEntity user) {

        
        List<CartEntity> list = cartRepository.findAllByUser(user);

        List<CartDto> responce = new ArrayList<>();
        for (CartEntity cart : list) {
            CartDto cartDto = modelMapper.map(cart.getPublication(), CartDto.class);
            cartDto.setSelectedCount(cart.getCount());

            SectionEntity sectionImage =
                    sectionRepository.findFirstByPublicationAndType(cart.getPublication(), SecType.PHOTO);
            if (sectionImage != null) {
                cartDto.setImageUrl(url + "/api/image/pub/" + sectionImage.getId());
            }

            responce.add(cartDto);
        }

        return responce;
    }
    public List<PublicationMinDto> getDrafts(UserEntity user) {

        List<PublicationMinDto> responce = new ArrayList<>();

        

        for (PublicationEntity p : publicationRepository.findAllByUserAndDraftIsTrueAndDeletedIsFalse(user)) {

            BigDecimal cal = calificationAverage(p);

            PublicationMinDto dto = modelMapper.map(p, PublicationMinDto.class);

            dto.setCalification(cal);
            dto.setDifficulty(Difficulty.values()[p.getDifficulty()].name());
            SectionEntity sectionImage = sectionRepository.findFirstByPublicationAndType(p, SecType.PHOTO);
            if (sectionImage != null) {
                dto.setImageUrl(url + "/api/image/pub/" + sectionImage.getId());
            }
            responce.add(dto);
        }

        return responce;
    }

    public PublicationDto get(Long id, UserEntity user) throws EntityNotFoundException {
        
        PublicationDto responce;
        PublicationEntity p = publicationRepository.getReferenceById(id);
        if ((p.isDeleted() || p.isDraft()) && !Objects.equals(p.getUser().getId(), user.getId())) {
            throw new EntityNotFoundException();
        }

        responce = modelMapper.map(p, PublicationDto.class);

        List<SectionDto> sections = new ArrayList<>();
        for (SectionEntity s : sectionRepository.findAllByPublication(p)) {
            SectionDto r = modelMapper.map(s, SectionDto.class);
            if (s.getImage() != null || s.getType().equals(SecType.PHOTO)) {
                r.setImageUrl(url + "/api/image/pub/" + r.getId());
            }
            sections.add(r);
        }
        responce.setUserId(p.getUser().getId());
        responce.setUsername(p.getUser().getUsername());
        responce.setUserIconUrl(url + "/api/image/user/" + p.getUser().getId());
        responce.setCalification(calificationAverage(p).toString());
        responce.setDifficulty(Difficulty.values()[p.getDifficulty()].name());
        responce.setDifficultyValue(p.getDifficulty());
        Optional<CalificationEntity> calificationEntity =
                calificationRepository.getByUserAndPublication(user, p);
        if (calificationEntity.isPresent()) {
            responce.setMyCalification(calificationEntity.get().getPoints().toString());
        } else {
            responce.setMyCalification("0");
        }

        responce.setSections(sections);

        return responce;
    }

    BigDecimal calificationAverage(PublicationEntity p) {
        List<CalificationEntity> list = calificationRepository.findAllByPublication(p);
        if (!list.isEmpty()) {
            BigDecimal value = BigDecimal.ZERO;
            int count = 0;
            for (CalificationEntity c : list) {
                value = value.add(c.getPoints());
                count++;
            }
            return value.divide(BigDecimal.valueOf(count), 1, RoundingMode.FLOOR);
        }
        return BigDecimal.ZERO;
    }

    public byte[] getImage(Long id) {

        SectionEntity s = sectionRepository.getReferenceById(id);
        if (s.getImage() != null && s.getImage().length > 0) {
            return decompressBytes(s.getImage());
        } else {
            Resource resource = new ClassPathResource("camera.png");

            try {
                InputStream in = resource.getInputStream();
                return IOUtils.toByteArray(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

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
            throw new RuntimeException(e.getMessage());
        }

        return outputStream.toByteArray();
    }

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
        } catch (IOException | DataFormatException e) {
            throw new RuntimeException(e.getMessage());
        }
        return outputStream.toByteArray();
    }

    //Put y delete
    @Transactional
    public PublicationDto put(PutPublicationRequest request, UserEntity user) {
        
        PublicationEntity publicationOld = publicationRepository.getReferenceById(request.getId());
        LocalDateTime date;
        if (publicationOld.isDeleted()) {
            throw new EntityNotFoundException();
        } else {
            date = publicationOld.getDateTime();
        }

        PublicationEntity publication = modelMapper.map(request, PublicationEntity.class);
        publication.setUser(user);
        publication.setDateTime(date);
        publicationRepository.save(publication);

        sectionRepository.deleteAllByPublication_Id(request.getId());
        List<SectionEntity> sectionEntities = new ArrayList<>();
        for (SectionRequest sectionDto : request.getSections()) {
            SectionEntity s = modelMapper.map(sectionDto, SectionEntity.class);
            s.setPublication(publication);

            sectionEntities.add(sectionRepository.save(s));
        }
        publication.setSections(sectionEntities);


        return get(publication.getId(), user);
    }

    @Transactional
    public boolean delete(Long id, UserEntity user) {
        
        PublicationEntity p = publicationRepository.getReferenceById(id);
        if(!p.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("Usuario incorrecto");
        }
        sectionRepository.deleteAllByPublication_Id(id);
        p.setDeleted(true);
        publicationRepository.save(p);
        return true;
    }
}
