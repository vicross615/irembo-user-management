package com.irembo.useraccountmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

/**
 * Created by USER on 5/8/2023.
 */
@Service
public class S3StorageService implements StorageService {
    private final S3Client s3Client;
    private static final String S3_BUCKET_NAME = "your-bucket-name";
    private static final String S3_FOLDER_NAME = "your-folder-name";
    @Autowired
    private AmazonS3 s3Client;

    public S3StorageService() {
        this.s3Client = S3Client.builder()
                .region(Region.of("your-region"))
                .build();
    }


    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public void store(String documentType, InputStream inputStream) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, documentType, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.Private);
        s3Client.putObject(putObjectRequest);
    }

    @Override
    public void storeDocument(String documentId, InputStream inputStream) {
        String key = S3_FOLDER_NAME + "/" + documentId;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(S3_BUCKET_NAME)
                .key(key)
                .serverSideEncryption("AES256")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream));
    }


    @Override
    public InputStream getDocument(String documentId) {
        String key = S3_FOLDER_NAME + "/" + documentId;

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(S3_BUCKET_NAME)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
    }
}