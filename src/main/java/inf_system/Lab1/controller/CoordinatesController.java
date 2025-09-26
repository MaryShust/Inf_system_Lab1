package inf_system.Lab1.controller;

import inf_system.Lab1.db.entities.Coordinates;
import inf_system.Lab1.db.repositories.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CoordinatesController {

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @GetMapping("/coordinates")
    public ResponseEntity<List<Coordinates>> getCoordinates() {
        return new ResponseEntity<>(coordinatesRepository.findAll(), HttpStatus.OK);
    }
}
