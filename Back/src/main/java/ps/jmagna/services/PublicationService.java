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
    public PublicationDto register(PublicationRequest request) {

        PublicationEntity publication = modelMapper.map(request, PublicationEntity.class);
        publication.setUser(userRepository.getReferenceById(request.getUser()));
        publication.setCreationTime(LocalDateTime.now());
        publicationRepository.save(publication);

        List<SectionEntity> sectionEntities = new ArrayList<>();
        for (SectionRequest sectionDto : request.getSections()) {
            SectionEntity s = modelMapper.map(sectionDto, SectionEntity.class);
            s.setPublication(publication);

            sectionEntities.add(sectionRepository.save(s));
        }
        publication.setSections(sectionEntities);


        return get(publication.getId(), publication.getUser().getId());
    }

    @Transactional
    public boolean registerImg(MultipartFile[] images, String indexes) throws IOException {

        int i = 0;
        for (String c : indexes.split("_")) {
            Long id = Long.parseLong(c);
            SectionEntity s = sectionRepository.getReferenceById(id);
            s.setImage(compressBytes(images[i].getBytes()));
            sectionRepository.save(s);
            i++;
        }

        return true;
    }

    public boolean calificate(CalificationRequest request) {
        PublicationEntity p = publicationRepository.getReferenceById(request.getPubId());
        UserEntity u = userRepository.getReferenceById(request.getUserId());
        Optional<CalificationEntity> calificationAsk = calificationRepository.getByUserAndPublication(u, p);
        CalificationEntity calification;
        if (calificationAsk.isEmpty()) {
            calification = new CalificationEntity();
            calification.setPublication(p);
            calification.setUser(u);
        } else {
            calification = calificationAsk.get();
        }

        calification.setPoints(request.getValue());
        calificationRepository.save(calification);

        return true;
    }

    public boolean addCart(CalificationRequest request) {
        PublicationEntity p = publicationRepository.getReferenceById(request.getPubId());
        UserEntity u = userRepository.getReferenceById(request.getUserId());
        Optional<CartEntity> cartAsk = cartRepository.getByUserAndPublication(u, p);

        int value = request.getValue().intValue();
        CartEntity cart;
        if (cartAsk.isEmpty()) {
            if (value <= 0) return false;
            cart = new CartEntity();
            cart.setPublication(p);
            cart.setUser(u);
        } else {
            if (value <= 0) {
                cartRepository.deleteById(cartAsk.get().getId());
                return true;
            }
            cart = cartAsk.get();
            cart.setCount(request.getValue().intValue());
        }
        cartRepository.save(cart);


        return true;
    }

    public SearchPubResponce getAll(SearchPubRequest request, String username) {

        SearchPubResponce responce = new SearchPubResponce();

        UserEntity u;
        if (username.contains("@")) {
            u = userRepository.getByEmail(username);
        } else {
            u = userRepository.getByUsername(username);
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<PublicationEntity> all = publicationRepository.findAll(createFilter(request, u), pageable);

        int count = (int) all.getTotalElements();
        List<PublicationMinDto> list = new ArrayList<>();
        for (PublicationEntity p : all) {

            /*if (p.isDeleted()) {
                continue;
            }
            if (request.getType() != PubType.NONE) {
                if (request.getType() != p.getType()) {
                    continue;
                }
            }
            if (p.getDifficulty() < request.getDiffMin() || p.getDifficulty() > request.getDiffMax()) {
                continue;
            }
            if (!request.getText().isBlank()) {
                List<String> textes = List.of(request.getText().toLowerCase()
                        .split("\\s"));
                boolean noadd = true;
                for (String t : textes) {
                    if (p.getName().toLowerCase().contains(t)) {
                        noadd = false;
                        break;
                    }
                    if (t.charAt(0) == '#' &&
                            p.getDescription().toLowerCase().contains(t)) {
                        noadd = false;
                        break;
                    }
                }
                if (noadd) {
                    continue;
                }
            }
            */

            BigDecimal cal = calificationAverage(p);
            if (cal.compareTo(request.getPoints()) < 0) {
                count--;
                continue;
            }

            if (!request.getMaterials().isBlank()) {
                List<String> mats = List.of(request.getMaterials().toLowerCase()
                        .split("[\\s,]+"));
                List<SectionEntity> smats = p.getSections().stream()
                        .filter(sec -> sec.getType().equals(SecType.MAT))
                        .toList();
                boolean noadd = true;
                for (SectionEntity m : smats) {
                    if (mats.contains(m.getText().toLowerCase())) {
                        noadd = false;
                        break;
                    }
                }
                if (noadd) {
                    count--;
                    continue;
                }
            }

            PublicationMinDto dto = modelMapper.map(p, PublicationMinDto.class);

            dto.setCalification(cal);
            dto.setDificulty(Difficulty.values()[p.getDifficulty()].name());
            SectionEntity sectionImage = sectionRepository.findFirstByPublicationAndType(p, SecType.PHOTO);
            if (sectionImage != null) {
                dto.setImageUrl(url + "/api/image/pub/" + sectionImage.getId());
            }
            list.add(dto);
        }
        list.sort(Comparator.comparing(PublicationMinDto::getCalification));

        responce.setList(list);

        responce.setCountTotal(count);

        return responce;
    }

    public static Specification<PublicationEntity> createFilter(SearchPubRequest request, UserEntity user) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isFalse(root.get("deleted")));

            Predicate difficulty = criteriaBuilder.between(
                    root.get("difficulty"),
                    request.getDiffMin(),
                    request.getDiffMax());
            predicates.add(difficulty);

            if (request.getType() != PubType.NONE) {
                predicates.add(criteriaBuilder.equal(root.get("type"), request.getType().toString()));
            }

            if (request.isMine()){
                predicates.add(criteriaBuilder.equal(root.get("user"), user));
            }

            if (!request.getText().isBlank()) {
                List<String> textes = List.of(request.getText().toLowerCase()
                        .split("\\s"));
                for (String t : textes) {
                    if (t.isBlank()) continue;
                    if (t.charAt(0) == '#') {
                        predicates.add(criteriaBuilder.like(root.get("description"), "%" + t + "%"));
                    }
                    else if(t.charAt(0) == '@'){
                        Join<PublicationEntity, UserEntity> join = root.join("user");
                        predicates.add(criteriaBuilder.equal(join.get("username"), t.substring(1)));
                    }
                    else {
                        predicates.add(criteriaBuilder.like(root.get("name"), "%" + t + "%"));
                    }
                }
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }; //columnEqual() function ends
    }

    //Listar
    public List<CartDto> getCart(String username) {

        UserEntity u;
        if (username.contains("@")) {
            u = userRepository.getByEmail(username);
        } else {
            u = userRepository.getByUsername(username);
        }
        List<CartEntity> list = cartRepository.findAllByUser(u);

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

    public PublicationDto get(Long id, Long userId) throws EntityNotFoundException {
        PublicationDto responce;
        PublicationEntity p = publicationRepository.getReferenceById(id);
        if (p.isDeleted() && !Objects.equals(p.getUser().getId(), userId)) {
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
                calificationRepository.getByUserAndPublication(userRepository.getReferenceById(userId), p);
        if (calificationEntity.isPresent()) {
            responce.setMyCalification(calificationEntity.get().getPoints().toString());
        } else {
            responce.setMyCalification("0");
        }

        responce.setSections(sections);

        return responce;
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
    public PublicationDto put(PutPublicationRequest request) {
        PublicationEntity publication = modelMapper.map(request, PublicationEntity.class);
        LocalDateTime date;
        if (!publicationRepository.existsById(publication.getId())
                && !publicationRepository.getReferenceById(publication.getId()).isDeleted()) {
            throw new EntityNotFoundException();
        } else {
            date = publicationRepository.getReferenceById(publication.getId()).getCreationTime();
        }
        publication.setUser(userRepository.getReferenceById(request.getUser()));
        publication.setCreationTime(date);
        publicationRepository.save(publication);

        sectionRepository.deleteAllByPublication_Id(request.getId());
        List<SectionEntity> sectionEntities = new ArrayList<>();
        for (SectionRequest sectionDto : request.getSections()) {
            SectionEntity s = modelMapper.map(sectionDto, SectionEntity.class);
            s.setPublication(publication);

            sectionEntities.add(sectionRepository.save(s));
        }
        publication.setSections(sectionEntities);


        return get(publication.getId(), publication.getUser().getId());
    }

    public boolean delete(Long id) {
        PublicationEntity p = publicationRepository.getReferenceById(id);
        p.setDeleted(true);
        publicationRepository.save(p);
        return true;
    }
}
