package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.NewUserRequest;
import ar.edu.utn.frc.tup.lciii.dtos.UserDto;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import ar.edu.utn.frc.tup.lciii.services.UserService;
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


    public List<UserDto> getAll(){
        List<UserDto> responce = new ArrayList<>();

        for (UserEntity u : userRepository.findAll()){
            responce.add(modelMapper.map(u, UserDto.class));
        }

        return responce;
    }
}
