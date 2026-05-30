package com.shayankhanani.connexio.service;

import com.shayankhanani.connexio.dto.user.UpdatePasswordDTO;
import com.shayankhanani.connexio.dto.user.UpdateUserDTO;
import com.shayankhanani.connexio.dto.user.UserDetailsDTO;
import com.shayankhanani.connexio.entity.User;
import com.shayankhanani.connexio.entity.Userprincipal;
import com.shayankhanani.connexio.exception.InvalidValueException;
import com.shayankhanani.connexio.exception.ResourceNotFoundException;
import com.shayankhanani.connexio.exception.auth.UserAlreadyExistException;
import com.shayankhanani.connexio.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService{


    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public UserDetailsDTO updateUserInfo(Userprincipal owner, UpdateUserDTO dto) {


        User user = userRepo.findById(owner.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found!!"));

        boolean exists = userRepo.existsByEmailOrUsernameOrPhone(
                dto.getEmail(),
                dto.getUsername(),
                dto.getPhone()
        );

        if (exists) {
            throw new UserAlreadyExistException(
                    "User already exists with email, username or phone"
            );
        }

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }

        if (dto.getLinkedinUrl() != null) {
            user.setLinkedinUrl(dto.getLinkedinUrl());
        }

        if (dto.getInstagramUrl() != null) {
            user.setInstagramUrl(dto.getInstagramUrl());
        }

        if (dto.getFacebookUrl() != null) {
            user.setFacebookUrl(dto.getFacebookUrl());
        }

        User updated = userRepo.save(user);

        return modelMapper.map(updated, UserDetailsDTO.class);
    }


    @Override
    public void updatePassword(Userprincipal owner, UpdatePasswordDTO dto) {

        User user = owner.getUser();

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidValueException("Incorrect old password provided");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new InvalidValueException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepo.save(user);
    }

}
