package com.shayankhanani.Connexio.service;

import com.shayankhanani.Connexio.entity.Userprincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthUtil {

    private Userprincipal getUser()
    {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (Userprincipal) authentication.getPrincipal();
    }

    public Long getUserId()
    {
        Userprincipal user = getUser();
        return user.getUserId();
    }


}

