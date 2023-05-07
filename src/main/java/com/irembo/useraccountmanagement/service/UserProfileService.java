package com.irembo.useraccountmanagement.service;


import com.irembo.useraccountmanagement.models.UserProfile;

/**
 * Created by USER on 5/5/2023.
 */
public interface UserProfileService {
    UserProfile createOrUpdateUserProfile(UserProfile userProfile);
    UserProfile findByUserId(Long userId);
}