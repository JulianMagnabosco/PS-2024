package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.ListUsersResponce;
import ar.edu.utn.frc.tup.lciii.dtos.SearchPubResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PutUserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.UserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.entities.StateEntity;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.enums.UserRole;
import ar.edu.utn.frc.tup.lciii.repository.StateRepository;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.compress.utils.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
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
import java.util.Optional;


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
    LoginResponce responce = new LoginResponce();

    UserEntity u = repository.getByUsername(username);
    responce.setId(u.getId());
    responce.setUsername(u.getUsername());
    responce.setEmail(u.getEmail());
    responce.setRole(u.getRole().toString());
    responce.setIconUrl(url + "/api/image/user/" + u.getId());

    return responce;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    //        UserEntity u1 = new UserEntity();
//        u1.setUsername("pablo");
//        u1.setPassword(passwordEncoder.encode("pablo"));
//    System.out.println("passp");
//    return User.builder()
//            .username("pablo")
//            .password(passwordEncoder.encode("pablo"))
//            .roles("USER")
//            .build();
    if(username.contains("@")){
      var user = repository.findByEmail(username);
      return user;
    }
    var user = repository.findByUsername(username);
    return user;
  }

  public UserDetails signUp(UserRequest data) {

    if (repository.findByUsername(data.getUsername()) != null) {
      throw new RuntimeException("Username alredy exists");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    UserEntity newUser = new UserEntity(data.getUsername(), data.getEmail(),
            encryptedPassword, data.getRole(), LocalDateTime.now());
    newUser.setState(stateRepository.getReferenceById(1L));

    return repository.save(newUser);

  }

  public ListUsersResponce getAll(String text, int page, int size){

    ListUsersResponce responce = new ListUsersResponce();
    List<UserDto> list = new ArrayList<>();

    Pageable pageable = PageRequest.of(page, size);
    Page<UserEntity> listRaw = repository.findAll(pageable);
    for (UserEntity u : listRaw.stream().toList()){
      if((!u.getName().isEmpty() && u.getName().contains(text)) ||
              !u.getLastname().isEmpty() && u.getLastname().contains(text) ||
              u.getUsername().contains(text))
      {
        UserDto r = getUserDto(u);
        list.add(r);
      }
    }

//    int firstIndex= page* size;
//    firstIndex=Integer.min(firstIndex,  list.size());
//
//    int lastIndex= firstIndex + size;
//    lastIndex=Integer.min(lastIndex, list.size());

    responce.setList(list);
//    responce.setList(list.subList(firstIndex,lastIndex));

    responce.setCountTotal((int) listRaw.getTotalElements());
//    responce.setCountTotal(list.size());

    return responce;
  }
  public ListUsersResponce getDealers(){

    ListUsersResponce responce = new ListUsersResponce();
    List<UserDto> list = new ArrayList<>();

    List<UserEntity> listRaw = repository.findAllByRole(UserRole.DELIVERY);
    for (UserEntity u : listRaw){
      UserDto r = getUserDto(u);
      list.add(r);
    }


    responce.setList(list);

    responce.setCountTotal(list.size());

    return responce;
  }
  public UserDto get(Long id){
    UserDto responce;

    UserEntity u = repository.getReferenceById(id);
    responce = getUserDto(u);

    return responce;
  }

  private UserDto getUserDto(UserEntity u) {
    UserDto responce;
    responce = modelMapper.map(u, UserDto.class);
    if(u.getState()!=null){
      responce.setIdState(u.getState().getId());
      responce.setState(u.getState().getName());
    }
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