package com.irembo.useraccountmanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by USER on 5/8/2023.
 */
public class LocalStorageService implements StorageService {

    @Value("${app.upload.dir:${user.home}}")
    private String uploadDirectory;

    private static final String LOCAL_STORAGE_PATH = "your-local-storage-path";

    @Override
    public void storeDocument(String documentId, InputStream inputStream) {
        File targetFile = new File(LOCAL_STORAGE_PATH + File.separator + documentId);
        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store document locally", e);
        }
    }

    @Override
    public InputStream getDocument(String documentId) {
        File file = new File(LOCAL_STORAGE_PATH + File.separator + documentId);
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve document from local storage", e);
        }
    }

    @Override
    public void store(String documentType, InputStream inputStream) throws IOException {
        String filePath = uploadDirectory + File.separator + documentType;
        Path copyLocation = Paths.get(StringUtils.cleanPath(filePath));
        Files.copy(inputStream, copyLocation, StandardCopyOption.REPLACE_EXISTING);
    }
}
