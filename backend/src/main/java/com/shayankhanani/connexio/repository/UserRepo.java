package com.shayankhanani.connexio.repository;

import com.shayankhanani.connexio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {

    User findByUsername(String username);
    boolean existsByEmailOrUsernameOrPhone(String email, String username, String phone);
}
