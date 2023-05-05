package com.irembo.useraccountmanagement.repo;

import com.irembo.useraccountmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Created by USER on 5/2/2023.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}