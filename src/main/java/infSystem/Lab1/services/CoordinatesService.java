package infSystem.Lab1.services;

import infSystem.Lab1.db.entities.Coordinates;
import infSystem.Lab1.db.repositories.CoordinatesRepository;
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