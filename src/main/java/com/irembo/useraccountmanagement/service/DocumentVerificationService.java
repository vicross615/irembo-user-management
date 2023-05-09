package com.irembo.useraccountmanagement.service;


import com.irembo.useraccountmanagement.models.DocumentVerification;
import com.irembo.useraccountmanagement.models.VerificationStatus;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by USER on 5/5/2023.
 */
public interface DocumentVerificationService {
    DocumentVerification submitVerification(DocumentVerification documentVerification);
    DocumentVerification findByUserId(Long userId);
    DocumentVerification updateVerificationStatus(Long userId, VerificationStatus status);
    List<DocumentVerification> getUserDocuments(Long userId);

    String storeDocument(String documentType, InputStream inputStream) throws IOException;

    InputStream encryptDocument(InputStream inputStream, SecretKey secretKey) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException;
}