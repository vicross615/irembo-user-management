package com.irembo.useraccountmanagement.repository;

/**
 * Created by USER on 5/5/2023.
 */
import com.irembo.useraccountmanagement.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long userId);
}
