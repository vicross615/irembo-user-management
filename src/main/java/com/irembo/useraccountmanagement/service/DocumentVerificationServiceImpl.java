package com.irembo.useraccountmanagement.service;

import com.irembo.useraccountmanagement.models.DocumentVerification;
import com.irembo.useraccountmanagement.models.User;
import com.irembo.useraccountmanagement.models.VerificationStatus;
import com.irembo.useraccountmanagement.repository.DocumentVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

/**
 * Created by USER on 5/5/2023.
 */
@Service
public class DocumentVerificationServiceImpl implements DocumentVerificationService {

    @Autowired
    private DocumentVerificationRepository documentVerificationRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private StorageService storageService;

    @Override
    public DocumentVerification submitVerification(DocumentVerification documentVerification) {
        User user = documentVerification.getUser();
        user.setVerificationStatus(VerificationStatus.PENDING_VERIFICATION);
        userService.updateUser(user);
        return documentVerificationRepository.save(documentVerification);
    }

//    @Override
//    public String getDocumentPath(String documentId) {
//        return storageService.getDocumentPath(documentId);
//    }


    @Override
    public DocumentVerification findByUserId(Long userId) {
        return documentVerificationRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public DocumentVerification updateVerificationStatus(Long userId, VerificationStatus status) {
        DocumentVerification documentVerification = findByUserId(userId);
        if (documentVerification != null) {
            User user = documentVerification.getUser();
            user.setVerificationStatus(status);
            userService.updateUser(user);
        }
        return documentVerification;
    }

//    @Override
//    public String getDocumentPath(String documentId) {
//        return null;
//    }

    @Override
    public List<DocumentVerification> getUserDocuments(Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        return documentVerificationRepository.findByUser(user);
    }

    @Override
    public String storeDocument(String documentType, InputStream inputStream) throws IOException {
        storageService.store(documentType, inputStream);
        return documentType;
    }
    @Override
    public InputStream encryptDocument(InputStream inputStream, SecretKey secretKey) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        cipherInputStream.close();
        outputStream.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}