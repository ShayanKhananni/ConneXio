package com.shayankhanani.Connexio.services;

import com.shayankhanani.Connexio.entity.User;
import com.shayankhanani.Connexio.entity.Userprincipal;
import com.shayankhanani.Connexio.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);

        if(user == null)
        {
            System.out.println("User not found!!");
            throw new UsernameNotFoundException("Username not Found!!");
        }

        return new Userprincipal(user);
    }
}
