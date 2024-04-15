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
import ar.edu.utn.frc.tup.lciii.repository.StateRepository;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
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
    var user = repository.findByUsername(username);
    return user;
  }

  public UserDetails signUp(UserRequest data) {

    if (repository.findByUsername(data.getUsername()) != null) {
      throw new RuntimeException("Username alredy exists");
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
        r.setIconUrl(url + "/api/image/user/" + u.getId());
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
    responce.setIconUrl(url + "/api/image/user/" + u.getId());

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
    newUser.setIcon(icon.getBytes());

    repository.save(newUser);
    return get(newUser.getId());
  }

}