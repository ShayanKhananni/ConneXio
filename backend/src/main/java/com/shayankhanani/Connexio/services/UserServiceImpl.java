package com.shayankhanani.Connexio.services;


import com.shayankhanani.Connexio.DTO.User.AddUserDTO;
import com.shayankhanani.Connexio.DTO.User.UserDTO;
import com.shayankhanani.Connexio.entity.User;
import com.shayankhanani.Connexio.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService{


    private final UserRepo userRepo;
    private final ModelMapper modelMapper;



    /// without mapper

//    @Override
//    public UserDTO addUser(AddUserDTO addUserDTO) {
//
//        User newUser = new User();
//        newUser.setPhone(addUserDTO.getPhone());
//        newUser.setUsername(addUserDTO.getUsername());
//        newUser.setEmail(addUserDTO.getEmail());
//
//        User createdUser = userRepo.save(newUser);
//
//        UserDTO savedUser = new UserDTO();
//        savedUser.setUserId(createdUser.getUserId());
//        savedUser.setUsername(createdUser.getUsername());
//        savedUser.setEmail(createdUser.getEmail());
//        savedUser.setPhone(createdUser.getPhone());
//
//        return savedUser;
//    }


    // with mapper
    @Override
    public UserDTO addUser(AddUserDTO addUserDTO) {
        User newUser = modelMapper.map(addUserDTO,User.class);
        User savedUser = userRepo.save(newUser);
        return modelMapper.map(savedUser,UserDTO.class);
    }

}
