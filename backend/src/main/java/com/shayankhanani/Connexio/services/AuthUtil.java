package com.shayankhanani.Connexio.services;

import com.shayankhanani.Connexio.entity.Userprincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public Long getUserId()
    {
        return ((Userprincipal)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal())
                .getUserId();
    }

    public Userprincipal getUser()
    {
        return ((Userprincipal)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal());
    }



}

