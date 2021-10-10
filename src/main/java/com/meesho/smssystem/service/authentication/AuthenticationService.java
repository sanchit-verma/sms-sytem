package com.meesho.smssystem.service.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class AuthenticationService {
    @Value("${server.authentication.field.value}")
    private String authenticationKey;
    public void authenticator(String authKey) throws AuthenticationException{
        if(!authKey.equals(authenticationKey))
            throw new AuthenticationException("Invalid Access Token");
    }
}
