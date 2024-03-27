package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.LoginRequest;
import ar.edu.utn.frc.tup.lciii.dtos.LoginResponce;
import ar.edu.utn.frc.tup.lciii.dtos.NewUserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    public UserDto register(NewUserRequest request){
        UserDto responce;

        Optional<UserEntity> userEntityO = userRepository.findByEmailOrName(request.getEmail(),request.getName());

        if(userEntityO.isPresent()){
            throw new IllegalArgumentException();
        }

        UserEntity user = modelMapper.map(request, UserEntity.class);

        userRepository.save(user);
        responce = modelMapper.map(user, UserDto.class);

        return responce;
    }

    public LoginResponce login(LoginRequest request){

        Optional<UserEntity> userEntityO = userRepository.findByName(request.getName());

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

        for (UserEntity u : userRepository.findAll()){
            responce.add(modelMapper.map(u, UserDto.class));
        }

        return responce;
    }
}
