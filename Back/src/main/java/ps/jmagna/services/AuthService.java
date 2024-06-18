package ps.jmagna.services;

import jakarta.persistence.EntityNotFoundException;
import org.antlr.v4.runtime.Token;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import ps.jmagna.dtos.user.*;
import ps.jmagna.entities.StateEntity;
import ps.jmagna.entities.TokenEntity;
import ps.jmagna.entities.UserEntity;
import ps.jmagna.enums.UserRole;
import ps.jmagna.exceptions.InvalidJwtException;
import ps.jmagna.repository.StateRepository;
import ps.jmagna.repository.TokenRepository;
import ps.jmagna.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.utils.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ps.jmagna.services.PublicationService.compressBytes;
import static ps.jmagna.services.PublicationService.decompressBytes;


@Service
public class AuthService implements UserDetailsService {

  @Autowired
  UserRepository repository;
  @Autowired
  StateRepository stateRepository;
  @Autowired
  TokenRepository tokenRepository;
  @Autowired
  NotificationService notificationService;
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  ObjectMapper objectMapper;

  @Value("${app.url}")
  private String url;

  //  Login
  public LoginResponce login(String username) {
    LoginResponce responce;

    UserEntity u;
    if(username.contains("@")){
      u = repository.getByEmail(username);
    }
    else {
      u = repository.getByUsername(username);
    }
    responce = modelMapper.map(u,LoginResponce.class);
    responce.setCanBuy(userCanBuy(u));
    responce.setCanSell(userCanSell(u));
    responce.setIconUrl(url + "/api/image/user/" + u.getId());

    return responce;
  }
  @Override
  public UserDetails loadUserByUsername(String username) {
    UserDetails user;
    if(username.contains("@")){
      user = repository.findByEmail(username);
    }else {
      user = repository.findByUsername(username);
    }
    if(user==null) throw new BadCredentialsException("");
    return user;
  }
  public UserEntity findUser(Jwt authentication) {
    String username=authentication.getSubject();
    UserEntity user;
    if(username.contains("@")){
      user = repository.getByEmail(username);
    }else {
      user = repository.getByUsername(username);
    }
    if(user==null) throw new BadCredentialsException("");
    return user;
  }
  public UserDto register(UserRequest data) {

    if (repository.findByUsername(data.getUsername()) != null) {
      throw new RuntimeException("Username alredy exists");
    }
    if (repository.findByEmail(data.getEmail()) != null) {
      throw new RuntimeException("Email alredy exists");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    UserEntity newUser = new UserEntity(data.getUsername(), data.getEmail(),
            encryptedPassword, UserRole.USER, LocalDateTime.now());
    newUser.setState(stateRepository.getReferenceById(1L));

    newUser = repository.save(newUser);

    notificationService.sendNotification("register_"+newUser.getId(),
            "Nueva cuenta de Como lo hago",
            "Bienvenido "+newUser.getUsername(),
            "Se a creado una nueva cuenta con este email, de nombre: "+
            newUser.getUsername(),
            newUser);

    return mapUserDto(newUser,true);
  }
  public UserTestResponce testSingUp(UserRequest data){
    UserTestResponce userTestResponce = new UserTestResponce();
    userTestResponce.setOkName(true);
    userTestResponce.setOkEmail(true);
    if (repository.findByUsername(data.getUsername()) != null) {
      userTestResponce.setOkName(false);
    }
    if (repository.findByEmail(data.getEmail()) != null) {
      userTestResponce.setOkEmail(false);
    }

    int points=0;
    if(!data.getPassword().matches(".*\\s.*")){
      if(data.getPassword().matches(".*[a-z].*")){
        points++;
      }
      if(data.getPassword().matches(".*[A-Z].*")){
        points++;
      }
      if(data.getPassword().matches(".*[0-9].*")){
        points++;
      }
      if(data.getPassword().matches(".*[^a-zA-Z0-9].*")){
        points++;
      }
    }

    userTestResponce.setPoints(points);
    return userTestResponce;
  }

  public boolean requestPasswordToken(String email) {

    TokenEntity token = new TokenEntity();
    token.setEmail(email);
    UUID uuid = tokenRepository.save(token).getUuid();

    UserEntity user = repository.getByEmail(email);
    if(user==null){
      throw new EntityNotFoundException();
    }

    boolean result = notificationService.sendNotification("password_req_"+user.getId()+"_"+uuid.toString(),
            "Cambio de contraseña",
            "Se ha solicitado un cambio de contraseña",
            "Aqui esta su token para cambio de contraseña " +
            "(introduzcalo en el campo token): "+ uuid,
            user);

    System.out.println(result);

    return true;
  }

  public boolean changePassword(PasswordRequest data) {

    TokenEntity tokenEntity = tokenRepository.getReferenceById(UUID.fromString(data.getToken()));
    if(!tokenEntity.getEmail().equals(data.getEmail())) return false;

    UserEntity user= repository.getByEmail(data.getEmail());
    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    user.setPassword(encryptedPassword);
    repository.save(user);

    notificationService.sendNotification("password_change_"+user.getId()+"_"+tokenEntity.getUuid(),
            "Cambio de contraseña",
            "Se cambio la contraseña",
            "Se cambio la contraseña",
            user);

    return true;
  }

  //  Get
  public ListUsersResponce getAll(String text, int page, int size){

    ListUsersResponce responce = new ListUsersResponce();
    List<UserMinDto> list = new ArrayList<>();

    Pageable pageable = PageRequest.of(page, size);
    Page<UserEntity> listRaw = repository.findAll(pageable);
    for (UserEntity u : listRaw.stream().toList()){
      if((!u.getName().isEmpty() && u.getName().contains(text)) ||
              !u.getLastname().isEmpty() && u.getLastname().contains(text) ||
              u.getUsername().contains(text))
      {
        UserMinDto r = mapUserMinDto(u);
        list.add(r);
      }
    }

    responce.setList(list);
    responce.setCountTotal((int) listRaw.getTotalElements());
    return responce;
  }
  public ListUsersResponce getDealers(){
    ListUsersResponce responce = new ListUsersResponce();
    List<UserMinDto> list = new ArrayList<>();

    List<UserEntity> listRaw = repository.findAllByRole(UserRole.DELIVERY);
    for (UserEntity u : listRaw){
      UserMinDto r = mapUserMinDto(u);
      list.add(r);
    }

    responce.setList(list);
    responce.setCountTotal(list.size());
    return responce;
  }
  public List<StateEntity> getStates() {
    return stateRepository.findAll();
  }
  public UserDto get(Long id, Jwt authentication){
    UserDto responce;
    ;
    UserEntity u = repository.getReferenceById(id);
    responce = mapUserDto(u,findUser(authentication).getId().equals(id));

    return responce;
  }
  static boolean userCanBuy(UserEntity user) {

    if (user.getName() == null || user.getName().isBlank() ||
            user.getLastname() == null || user.getLastname().isBlank() ||
            user.getPhone() == null || user.getPhone().isBlank() ||
            user.getDni() == null || user.getDni().isBlank() ||
            user.getDniType() == null || user.getDniType().isBlank() ||
            user.getState() == null || user.getState().getId() == 1L ||
            user.getDirection() == null || user.getDirection().isBlank() ||
            user.getNumberDir() == null || user.getNumberDir().isBlank() ||
            user.getPostalNum() == null || user.getPostalNum().isBlank()) {
      return false;
    }

    return true;
  }
  static boolean userCanSell(UserEntity user) {

    if (user.getCvu() == null || user.getCvu().isBlank()) {
      return false;
    }

    return true;
  }
  private UserDto mapUserDto(UserEntity u, boolean sameUser) {
    UserDto responce = modelMapper.map(u, UserDto.class);
    if(u.getState()!=null){
      responce.setIdState(u.getState().getId());
      responce.setState(u.getState().getName());
    }
    responce.setSame(sameUser);
    responce.setIconUrl(url + "/api/image/user/" + u.getId());
    return responce;
  }
  private UserMinDto mapUserMinDto(UserEntity u) {
    UserMinDto responce = modelMapper.map(u, UserMinDto.class);
    responce.setIconUrl(url + "/api/image/user/" + u.getId());
    return responce;
  }

  //  Image
  public byte[] getImage(Long id){
    byte[] responce;

    UserEntity u = repository.getReferenceById(id);
    responce = u.getIcon();
    if(responce!=null && responce.length>0)   {
      return decompressBytes(responce);
    }
    else {
      Resource resource = new ClassPathResource("user.png");

      try {
        InputStream in = resource.getInputStream();
        return IOUtils.toByteArray(in);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  //  Put
  public UserDto put(String requestRaw, MultipartFile icon, Jwt authentication) throws IOException {
    UserEntity user = findUser(authentication);

    PutUserRequest request = objectMapper.readValue(requestRaw, PutUserRequest.class);
    if(!user.getId().equals(request.getId())) {
      throw new IllegalArgumentException("Usuario incorrecto");
    }
    UserEntity newUser = modelMapper.map(request, UserEntity.class);
    newUser.setRole(user.getRole());
    newUser.setDateTime(user.getDateTime());

//    if(request.isChangePass()){
//      String encryptedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
//      newUser.setPassword(encryptedPassword);
//    }else {
//      newUser.setPassword(user.getPassword());
//    }
    if(!user.getState().getId().equals(request.getIdState())){
      StateEntity state = stateRepository.getReferenceById(request.getIdState());
      newUser.setState(state);
    }
    if(icon!=null){
      newUser.setIcon(compressBytes(icon.getBytes()));
    }

    return mapUserDto(repository.save(newUser),true);
  }

  public UserDto putRole(Long id, UserRole role, Jwt authentication) {
    UserEntity user = findUser(authentication);

    if(!user.getRole().equals(UserRole.ADMIN)) {
      throw new IllegalArgumentException("Usuario sin permisos");
    }

    UserEntity newUser = repository.getReferenceById(id);

    newUser.setRole(role);

    return mapUserDto(repository.save(newUser),true);
  }
}