package inf_system.Lab1.services;

import inf_system.Lab1.db.entities.Coordinates;
import inf_system.Lab1.db.repositories.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoordinatesService {

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    public List<Coordinates> getAllCoordinates() {
        return coordinatesRepository.findAll();
    }
}