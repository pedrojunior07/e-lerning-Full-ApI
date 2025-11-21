package com.elearning.e_learning_core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.elearning.e_learning_core.Repository.UserRepository;
import com.elearning.e_learning_core.model.Usr;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Usr getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
