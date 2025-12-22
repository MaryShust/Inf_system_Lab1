package infs.lab.services;

import infs.lab.config.MinioConfig;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import infs.lab.config.AppConfig;

@Slf4j
@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private AppConfig appConfig;

    @PostConstruct
    public void init() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .build()
                );

                log.info("Bucket создан успешно");
            } else {
                log.info("Bucket уже создан");
            }
        } catch (Exception e) {
            log.error("Ошибка при проверке/создании бакета: " + e.getMessage());
        }
    }

    public String uploadFile(MultipartFile file) throws IOException,
            ServerException, InsufficientDataException, ErrorResponseException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String objectName = UUID.randomUUID() + fileExtension;

        String contentType = file.getContentType();
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );
        }

        log.info("Файл загружен в MinIO");
        return objectName;
    }

    public String getFileUrl(String objectName) throws ServerException,
            InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {

        if (objectName == null || objectName.trim().isEmpty()) {
            return null;
        }

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(appConfig.getMethod())
                        .bucket(minioConfig.getBucketName())
                        .object(objectName)
                        .expiry(appConfig.getPresignedUrlExpiryDays(), appConfig.getPresignedUrlExpiryTimeUnit()) // Срок действия ссылки
                        .build()
        );
    }

    public void deleteFile(String objectName) throws ServerException,
            InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        if (objectName == null || objectName.trim().isEmpty()) {
            return;
        }
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .object(objectName)
                        .build()
        );
        log.info("Файл удален из MinIO");
    }

    public boolean fileExists(String objectName) {
        try {
            if (objectName == null || objectName.trim().isEmpty()) {
                return false;
            }

            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getFileDownloadUrl(String objectName, String originalFilename) throws ServerException,
            InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {

        if (objectName == null || objectName.trim().isEmpty()) {
            return null;
        }

        String filename = (originalFilename != null && !originalFilename.trim().isEmpty())
                ? originalFilename
                : objectName;

        String responseContentDisposition = "attachment; filename=\"" + filename + "\"";

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(appConfig.getMethod())
                        .bucket(minioConfig.getBucketName())
                        .object(objectName)
                        .expiry(appConfig.getPresignedUrlExpiryDays(), appConfig.getPresignedUrlExpiryTimeUnit())
                        .extraQueryParams(Map.of(
                                "response-content-disposition", responseContentDisposition
                        ))
                        .build()
        );
    }
}