package com.irembo.useraccountmanagement.service;

import com.irembo.useraccountmanagement.dto.UserRegistrationDto;
import com.irembo.useraccountmanagement.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created by USER on 5/2/2023.
 */
public interface UserService extends UserDetailsService {

    User register(UserRegistrationDto registrationDto);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();
}
