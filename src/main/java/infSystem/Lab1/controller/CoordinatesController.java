package infSystem.Lab1.controller;

import infSystem.Lab1.db.entities.Coordinates;
import infSystem.Lab1.services.CoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class CoordinatesController {

    @Autowired
    private CoordinatesService coordinatesService;

    @GetMapping("/coordinates")
    public ResponseEntity<List<Coordinates>> getCoordinates() {
        List<Coordinates> coordinates = coordinatesService.getAllCoordinates();
        return ResponseEntity.ok(coordinates);
    }
}