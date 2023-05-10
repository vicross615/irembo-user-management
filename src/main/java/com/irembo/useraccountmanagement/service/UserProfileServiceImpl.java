package com.irembo.useraccountmanagement.service;

import com.irembo.useraccountmanagement.models.UserProfile;
import com.irembo.useraccountmanagement.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by USER on 5/5/2023.
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public UserProfile createOrUpdateUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile findByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId); }
}