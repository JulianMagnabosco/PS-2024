package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.requests.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.requests.UserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AuthService implements UserDetailsService {

  @Autowired
  UserRepository repository;


  @Autowired
  ModelMapper modelMapper;

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
      r.setName("ERROR");
      return r;
    }

    r.setName("ok");
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

  public List<UserDto> getAll(String text){
    List<UserDto> responce = new ArrayList<>();

    for (UserEntity u : repository.findAll()){
      if(u.getName().contains(text) || u.getLastname().contains(text) || u.getUsername().contains(text))
        responce.add(modelMapper.map(u, UserDto.class));
    }

    return responce;
  }
  public UserDto get(Long id){
    UserDto responce;

    UserEntity u = repository.getReferenceById(id);
    responce = modelMapper.map(u, UserDto.class);

    return responce;
  }
}