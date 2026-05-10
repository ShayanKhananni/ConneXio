package com.shayankhanani.Connexio.repository;

import com.shayankhanani.Connexio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {

    User findByUsername(String username);
}
