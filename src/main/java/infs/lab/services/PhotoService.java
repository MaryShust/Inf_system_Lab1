package infs.lab.services;

import infs.lab.controller.dto.PersonDTO;
import infs.lab.controller.dto.PhotoResponse;
import infs.lab.controller.exception.NotFoundException;
import infs.lab.controller.exception.ValidationException;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class PhotoService {

    @Autowired
    private PersonService personService;
    @Autowired
    private MinioService minioService;

    @Transactional
    public PhotoResponse uploadPhoto(Long id, MultipartFile file) throws ServerException,
            InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {

        PersonDTO personDTO = personService.findPerson(id);

        String oldPhotoId = personDTO.getPhotoId();
        if (oldPhotoId != null && !oldPhotoId.trim().isEmpty()) {
            try {
                minioService.deleteFile(oldPhotoId);
            } catch (Exception e) {
                System.out.println("Не удалось удалить старое фото: " + e.getMessage());
            }
        }

        String newPhotoId = minioService.uploadFile(file);
        personDTO.setPhotoId(newPhotoId);
        personService.updatePerson(personDTO);

        String photoUrl = minioService.getFileUrl(newPhotoId);
        return new PhotoResponse("Фото успешно загружено", newPhotoId, photoUrl);
    }

    @Transactional
    public PhotoResponse getPhoto(@PathVariable String photoId) throws ServerException,
            InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        if (photoId == null || photoId.trim().isEmpty()) {
            throw new ValidationException("PhotoId не указан");
        }
        if (!minioService.fileExists(photoId)) {
            throw new NotFoundException("Фото не найдено");
        }

        String photoUrl = minioService.getFileUrl(photoId);
        return new PhotoResponse("Фото найдено", photoId, photoUrl);
    }

    @Transactional
    public PhotoResponse deletePhoto(Long id) throws ServerException,
            InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {

        PersonDTO personDTO = personService.findPerson(id);

        String oldPhotoId = personDTO.getPhotoId();
        if (oldPhotoId != null && !oldPhotoId.trim().isEmpty()) {
            try {
                minioService.deleteFile(oldPhotoId);
            } catch (Exception e) {
                System.out.println("Не удалось удалить старое фото: " + e.getMessage());
            }
        }

        personDTO.setPhotoId(null);
        personService.updatePerson(personDTO);

        return new PhotoResponse("Фото успешно удалено", /* photoId */ null, /* photoUrl */ null);
    }
}