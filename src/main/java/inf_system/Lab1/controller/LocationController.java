package inf_system.Lab1.controller;

import inf_system.Lab1.db.entities.Location;
import inf_system.Lab1.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> coordinates = locationService.getAllLocations();
        return ResponseEntity.ok(coordinates);
    }
}