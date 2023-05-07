package com.irembo.useraccountmanagement.service;


import com.irembo.useraccountmanagement.models.User;

/**
 * Created by USER on 5/5/2023.
 */
public interface MfaService {
    void generateAndSendMfaCode(String  email);
    boolean verifyMfaCode(String  email, String code);
}