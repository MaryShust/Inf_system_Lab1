package inf_system.Lab1.controller;

import inf_system.Lab1.db.entities.Location;
import inf_system.Lab1.db.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getLocations() {
        return new ResponseEntity<>(locationRepository.findAll(), HttpStatus.OK);
    }
}
