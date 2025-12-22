package infs.lab.services;

import infs.lab.controller.dto.PersonDTO;
import infs.lab.controller.dto.PhotoResponse;
import infs.lab.controller.exception.NotFoundException;
import infs.lab.controller.exception.ValidationException;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import infs.lab.controller.exception.WorkWithPhotoException;

@Service
@Slf4j
public class PhotoService {

    @Autowired
    private PersonService personService;
    @Autowired
    private MinioService minioService;

    @Transactional
    public PhotoResponse uploadPhoto(Long id, MultipartFile file) throws WorkWithPhotoException {
        try {
            PersonDTO personDTO = personService.findPerson(id);

            String oldPhotoId = personDTO.photoId();
            if (oldPhotoId != null && !oldPhotoId.trim().isEmpty()) {
                try {
                    minioService.deleteFile(oldPhotoId);
                } catch (Exception e) {
                    System.out.println("Не удалось удалить старое фото: " + e.getMessage());
                }
            }

            String newPhotoId = minioService.uploadFile(file);
            personService.updatePerson(new PersonDTO(
                    personDTO.id(),
                    personDTO.name(),
                    personDTO.coordinates(),
                    personDTO.eyeColor(),
                    personDTO.hairColor(),
                    personDTO.location(),
                    personDTO.height(),
                    personDTO.birthday(),
                    personDTO.nationality(),
                    personDTO.creationDate(),
                    newPhotoId
            ));

            String photoUrl = minioService.getFileUrl(newPhotoId);
            return new PhotoResponse("Фото успешно загружено", newPhotoId, photoUrl);
        } catch (ServerException | InsufficientDataException | ErrorResponseException |
                IOException | NoSuchAlgorithmException | InvalidKeyException |
                InvalidResponseException | XmlParserException | InternalException e) {
            throw new WorkWithPhotoException(e.getMessage());
        }
    }

    @Transactional
    public PhotoResponse getPhoto(@PathVariable String photoId) throws WorkWithPhotoException {
        try {
            if (photoId == null || photoId.trim().isEmpty()) {
                throw new ValidationException("PhotoId не указан");
            }
            if (!minioService.fileExists(photoId)) {
                throw new NotFoundException("Фото не найдено");
            }

            String photoUrl = minioService.getFileUrl(photoId);
            return new PhotoResponse("Фото найдено", photoId, photoUrl);
        } catch (ServerException | InsufficientDataException | ErrorResponseException |
                IOException | NoSuchAlgorithmException | InvalidKeyException |
                InvalidResponseException | XmlParserException | InternalException e) {
            throw new WorkWithPhotoException(e.getMessage());
        }
    }

    @Transactional
    public PhotoResponse deletePhoto(Long id)  {

        PersonDTO personDTO = personService.findPerson(id);

        String oldPhotoId = personDTO.photoId();
        if (oldPhotoId != null && !oldPhotoId.trim().isEmpty()) {
            try {
                minioService.deleteFile(oldPhotoId);
            } catch (Exception e) {
                log.error("Не удалось удалить старое фото: " + e.getMessage());
            }
        }

        personService.updatePerson(
                new PersonDTO(
                        personDTO.id(),
                        personDTO.name(),
                        personDTO.coordinates(),
                        personDTO.eyeColor(),
                        personDTO.hairColor(),
                        personDTO.location(),
                        personDTO.height(),
                        personDTO.birthday(),
                        personDTO.nationality(),
                        personDTO.creationDate(),
                        null
                )
        );

        return new PhotoResponse("Фото успешно удалено", /* photoId */ null, /* photoUrl */ null);
    }
}