package infs.lab.controller;

import infs.lab.controller.dto.PhotoResponse;
import infs.lab.controller.exception.WorkWithPhotoException;
import infs.lab.services.MinioService;
import infs.lab.services.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/persons")
public class PhotoController {

    @Autowired
    private MinioService minioService;
    @Autowired
    private PhotoService photoService;

    @PostMapping("/{id}/photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws WorkWithPhotoException {
        PhotoResponse response = photoService.uploadPhoto(id, file);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<?> getPhoto(@PathVariable String photoId) throws WorkWithPhotoException {
        PhotoResponse response = photoService.getPhoto(photoId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        PhotoResponse response = photoService.deletePhoto(id);
        return ResponseEntity.ok(response);
    }
}