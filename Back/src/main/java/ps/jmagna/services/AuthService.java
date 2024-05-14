package ps.jmagna.services;

import ps.jmagna.dtos.user.*;
import ps.jmagna.entities.StateEntity;
import ps.jmagna.entities.UserEntity;
import ps.jmagna.enums.UserRole;
import ps.jmagna.repository.StateRepository;
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


@Service
public class AuthService implements UserDetailsService {

  @Autowired
  UserRepository repository;
  @Autowired
  StateRepository stateRepository;
  @Autowired
  ModelMapper modelMapper;
  @Autowired
  ObjectMapper objectMapper;

  @Value("${app.url}")
  private String url;

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
    if(username.contains("@")){
      return repository.findByEmail(username);
    }
    return repository.findByUsername(username);
  }

  public UserDto signUp(UserRequest data) {
    UserDto responce;

    if (repository.findByUsername(data.getUsername()) != null) {
      throw new RuntimeException("Username alredy exists");
    }
    if (repository.findByEmail(data.getEmail()) != null) {
      throw new RuntimeException("Email alredy exists");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    UserEntity newUser = new UserEntity(data.getUsername(), data.getEmail(),
            encryptedPassword, data.getRole(), LocalDateTime.now());
    newUser.setState(stateRepository.getReferenceById(1L));

    responce=mapUserDto(newUser);
    return responce;
  }
  public UserTestResponce tesSingUp(UserRequest data){
    UserTestResponce userTestResponce = new UserTestResponce();
    userTestResponce.setOkName(true);
    userTestResponce.setOkEmail(true);
    if (repository.findByUsername(data.getUsername()) != null) {
      userTestResponce.setOkName(false);
    }
    if (repository.findByEmail(data.getEmail()) != null) {
      userTestResponce.setOkEmail(false);
    }

    int numberFlag=0;
    int capitalFlag=0;
    int lowerCaseFlag=0;
    for(Character ch : data.getPassword().toCharArray()) {
      if(numberFlag==0 && Character.isDigit(ch)) {
        numberFlag=1;
      }
      else {
        if (capitalFlag==0 && Character.isUpperCase(ch)) {
          capitalFlag=1;
        }
        else if (lowerCaseFlag==0 && Character.isLowerCase(ch)) {
          lowerCaseFlag=1;
        }
      }
    }
    userTestResponce.setPoints(numberFlag+capitalFlag+lowerCaseFlag);
    return userTestResponce;
  }

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

  public UserDto get(Long id){
    UserDto responce;

    UserEntity u = repository.getReferenceById(id);
    responce = mapUserDto(u);

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

    if (user.getMpClient() == null || user.getMpClient().isBlank() ||
            user.getMpSecret() == null || user.getMpSecret().isBlank()) {
      return false;
    }

    return true;
  }

  private UserDto mapUserDto(UserEntity u) {
    UserDto responce = modelMapper.map(u, UserDto.class);
    if(u.getState()!=null){
      responce.setIdState(u.getState().getId());
      responce.setState(u.getState().getName());
    }
    responce.setIconUrl(url + "/api/image/user/" + u.getId());
    return responce;
  }

  private UserMinDto mapUserMinDto(UserEntity u) {
    UserMinDto responce = modelMapper.map(u, UserMinDto.class);
    responce.setIconUrl(url + "/api/image/user/" + u.getId());
    return responce;
  }

  public byte[] getImage(Long id){
    byte[] responce;

    UserEntity u = repository.getReferenceById(id);
    responce = u.getIcon();
    if(responce!=null && responce.length>0)   {
      return responce;
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

  public UserDto put(String requestRaw, MultipartFile icon) throws IOException {

    PutUserRequest request = objectMapper.readValue(requestRaw, PutUserRequest.class);
    UserEntity user = repository.getReferenceById(request.getId());
    UserEntity newUser = modelMapper.map(request, UserEntity.class);

    if(request.isChangePass()){
      String encryptedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
      newUser.setPassword(encryptedPassword);
    }else {
      newUser.setPassword(user.getPassword());
    }
    newUser.setRole(user.getRole());
    if(!user.getState().getId().equals(request.getIdState())){
      StateEntity state = stateRepository.getReferenceById(request.getIdState());
      newUser.setState(state);
    }
    if(icon!=null){
      newUser.setIcon(icon.getBytes());
    }

    repository.save(newUser);
    return get(newUser.getId());
  }

}