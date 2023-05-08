package com.irembo.useraccountmanagement.repository;

import com.irembo.useraccountmanagement.models.DocumentVerification;
import com.irembo.useraccountmanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by USER on 5/5/2023.
 */
@Repository
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {
    Optional<DocumentVerification> findByUserId(Long userId);
    List<DocumentVerification> findByUser(User user);
}