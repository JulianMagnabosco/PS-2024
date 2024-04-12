package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.ListUsersResponce;
import ar.edu.utn.frc.tup.lciii.dtos.SearchPubResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.PutUserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.requests.UserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.repository.StateRepository;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

//  public UserDto oldregister(UserRequest request){
//    UserDto responce;
//
//    Optional<UserEntity> userEntityO = repository.findByEmailOrName(request.getEmail(),request.getName());
//
//    if(userEntityO.isPresent()){
//      throw new IllegalArgumentException();
//    }
//
//    UserEntity user = modelMapper.map(request, UserEntity.class);
//
//    repository.save(user);
//    responce = modelMapper.map(user, UserDto.class);
//
//    return responce;
//  }

  public LoginResponce login(LoginRequest request){

    LoginResponce r = new LoginResponce();
    Optional<UserEntity> userEntityO = repository.findFirstByUsername(request.getUsername());

    if(userEntityO.isEmpty()){
      throw new EntityNotFoundException();
    }

    if(!request.getPassword().equals(userEntityO.get().getPassword())){
      r.setUsername("ERROR");
      return r;
    }

    r.setUsername("ok");
    return r;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    var user = repository.findByUsername(username);
    return user;
  }

  public UserDetails signUp(UserRequest data) {

    if (repository.findByUsername(data.getUsername()) != null) {
      throw new EntityNotFoundException();
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    UserEntity newUser = new UserEntity(data.getUsername(), encryptedPassword, data.getRole());

    return repository.save(newUser);

  }

  public ListUsersResponce getAll(String text, int page, int size){

    ListUsersResponce responce = new ListUsersResponce();
    List<UserDto> list = new ArrayList<>();

    for (UserEntity u : repository.findAll()){
      if(u.getName().contains(text) || u.getLastname().contains(text) || u.getUsername().contains(text))
      {
        UserDto r = modelMapper.map(u, UserDto.class);
        r.setIdState(u.getState().getId());
        r.setState(u.getState().getName());
        r.setIconUrl(url + "/user/image/" + u.getId());
        list.add(r);
      }
    }

    int firstIndex= page* size;
    firstIndex=Integer.min(firstIndex,  list.size());

    int lastIndex= firstIndex + size;
    lastIndex=Integer.min(lastIndex, list.size());

    responce.setList(list.subList(firstIndex,lastIndex));

    responce.setCountTotal(list.size());

    return responce;
  }
  public UserDto get(Long id){
    UserDto responce;

    UserEntity u = repository.getReferenceById(id);
    responce = modelMapper.map(u, UserDto.class);
    responce.setIdState(u.getState().getId());
    responce.setState(u.getState().getName());
    responce.setIconUrl(url + "/user/image/" + u.getId());

    return responce;
  }

  public byte[] getImage(Long id){
    byte[] responce;

    UserEntity u = repository.getReferenceById(id);
    responce = u.getIcon();

    return responce;
  }

  public UserDto put(String requestRaw, MultipartFile icon) throws IOException {

    PutUserRequest request = objectMapper.readValue(requestRaw, PutUserRequest.class);
    UserEntity u = repository.getReferenceById(request.getId());

    if (u == null) {
      throw new EntityNotFoundException();
    }
    u = modelMapper.map(request, UserEntity.class);
    if(request.isChangePass()){
      String encryptedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
      u.setPassword(encryptedPassword);
    }
    u.setIcon(icon.getBytes());
    repository.save(u);
    return get(u.getId());
  }

}