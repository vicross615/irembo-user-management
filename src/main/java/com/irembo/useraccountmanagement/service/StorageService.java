package com.irembo.useraccountmanagement.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by USER on 5/8/2023.
 */
public interface StorageService {
    void storeDocument(String documentId, InputStream inputStream);
    InputStream getDocument(String documentId);
    void store(String documentType, InputStream inputStream) throws IOException;
}