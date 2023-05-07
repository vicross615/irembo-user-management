package com.irembo.useraccountmanagement.service;


import com.irembo.useraccountmanagement.models.DocumentVerification;
import com.irembo.useraccountmanagement.models.VerificationStatus;

/**
 * Created by USER on 5/5/2023.
 */
public interface DocumentVerificationService {
    DocumentVerification submitVerification(DocumentVerification documentVerification);
    DocumentVerification findByUserId(Long userId);
    DocumentVerification updateVerificationStatus(Long userId, VerificationStatus status);
}