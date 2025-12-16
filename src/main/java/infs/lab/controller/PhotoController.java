package infs.lab.controller;

import infs.lab.controller.dto.PhotoResponse;
import infs.lab.services.MinioService;
import infs.lab.services.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            PhotoResponse response = photoService.uploadPhoto(id, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<?> getPhoto(@PathVariable String photoId) {
        try {
            PhotoResponse response = photoService.getPhoto(photoId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            PhotoResponse response = photoService.deletePhoto(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}