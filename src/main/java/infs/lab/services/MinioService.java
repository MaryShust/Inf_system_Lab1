package infs.lab.services;

import infs.lab.config.MinioConfig;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfig minioConfig;

    public void ensureBucketExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .build());
                System.out.println("Bucket создан успешно");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при проверке/создании бакета: " + e.getMessage());
        }
    }

    public String uploadFile(MultipartFile file) throws IOException,
            ServerException, InsufficientDataException, ErrorResponseException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        ensureBucketExists();

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Генерируем уникальное имя файла
        String objectName = UUID.randomUUID() + fileExtension;

        // Определяем content type
        String contentType = file.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
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

        System.out.println("Файл загружен в MinIO");
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
                        .method(Method.GET)
                        .bucket(minioConfig.getBucketName())
                        .object(objectName)
                        .expiry(7, TimeUnit.DAYS) // Срок действия ссылки
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
        System.out.println("Файл удален из MinIO");
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

        // Используем оригинальное имя файла для скачивания
        String filename = (originalFilename != null && !originalFilename.trim().isEmpty())
                ? originalFilename
                : objectName;

        String responseContentDisposition = "attachment; filename=\"" + filename + "\"";

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(minioConfig.getBucketName())
                        .object(objectName)
                        .expiry(7, TimeUnit.DAYS)
                        .extraQueryParams(Map.of(
                                "response-content-disposition", responseContentDisposition
                        ))
                        .build()
        );
    }
}