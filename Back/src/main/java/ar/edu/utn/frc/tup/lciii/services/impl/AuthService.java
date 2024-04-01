package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.UserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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

    Optional<UserEntity> userEntityO = repository.findFirstByName(request.getName());

    if(userEntityO.isEmpty()){
      throw new EntityNotFoundException();
    }

    if(!request.getPassword().equals(userEntityO.get().getPassword())){
      return new LoginResponce("error","");
    }

    return new LoginResponce("ok","a");
  }


  public List<UserDto> getAll(){
    List<UserDto> responce = new ArrayList<>();

    for (UserEntity u : repository.findAll()){
      responce.add(modelMapper.map(u, UserDto.class));
    }

    return responce;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    var user = repository.findByName(username);
    return user;
  }

  public UserDetails signUp(UserRequest data) throws ChangeSetPersister.NotFoundException {

    if (repository.findByName(data.getName()) != null) {
      throw new ChangeSetPersister.NotFoundException();
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    UserEntity newUser = new UserEntity(data.getName(), encryptedPassword, data.getRole());

    return repository.save(newUser);

  }
}