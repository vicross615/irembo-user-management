package com.irembo.useraccountmanagement.controllers;

import com.irembo.useraccountmanagement.models.DocumentVerification;
import com.irembo.useraccountmanagement.models.User;
import com.irembo.useraccountmanagement.models.VerificationStatus;
import com.irembo.useraccountmanagement.service.DocumentVerificationService;
import com.irembo.useraccountmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by USER on 5/5/2023.
 */
@RestController
@RequestMapping("/verification")
public class DocumentVerificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentVerificationService documentVerificationService;

    @Value("${app.upload.dir:${user.home}}")
    private String uploadDirectory;

    @PostMapping("/{userId}/submit")
    public ResponseEntity<String> submitDocument(@PathVariable Long userId,
                                                 @RequestParam("documentType") String documentType,
                                                 @RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        User user = userService.findById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!file.isEmpty()) {
            try {
                // Store the document using DocumentVerificationService
                InputStream inputStream = file.getInputStream();
                InputStream encryptedInputStream = documentVerificationService.encryptDocument(inputStream, generateSecretKey());
                String documentPath = documentVerificationService.storeDocument(documentType, encryptedInputStream);

                DocumentVerification documentVerification = new DocumentVerification();
                documentVerification.setUser(user);
                documentVerification.setDocumentType(documentType);
                // Set the document path as the S3 object key or local file path
//                String documentPath = documentVerificationService.getDocumentPath(documentPath);
                documentVerification.setDocumentImagePath(documentPath);

                documentVerificationService.submitVerification(documentVerification);
                return new ResponseEntity<>("Document submitted successfully", HttpStatus.OK);

            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
    }

    public SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // You can use 128, 192 or 256 bits key
        return keyGenerator.generateKey();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{userId}/callback")
    public ResponseEntity<String> documentVerificationCallback(@PathVariable Long userId,
                                                               @RequestParam("status") VerificationStatus status) {
        DocumentVerification documentVerification = documentVerificationService.updateVerificationStatus(userId, status);
        if (documentVerification != null) {
            // Optionally, send a notification to the user when the account is verified
            if (status == VerificationStatus.VERIFIED) {
                // Send a notification to the user
            }
            return new ResponseEntity<>("Verification status updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")          
    @GetMapping("/{userId}/documents")
    public ResponseEntity<List<DocumentVerification>> getUserDocuments(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<DocumentVerification> userDocuments = documentVerificationService.getUserDocuments(userId);
        return new ResponseEntity<>(userDocuments, HttpStatus.OK);
    }

    //    @GetMapping("/documents")
//    public ResponseEntity<List<DocumentVerification>> getAllUserDocuments() {
//        List<DocumentVerification> allUserDocuments = documentVerificationService.getAllUserDocuments();
//        return new ResponseEntity<>(allUserDocuments, HttpStatus.OK);
//    }


}