package com.example.ecommerce.modules.user.services.impl;

import com.example.ecommerce.config.UserDetailsConfig;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.modules.user.entities.User;
import com.example.ecommerce.modules.user.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findById(Long.valueOf(userId));
        return user.map(UserDetailsConfig::new)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
