package infs.lab.services;

import infs.lab.aop.CacheLogging;
import infs.lab.db.entities.Coordinates;
import infs.lab.db.repositories.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoordinatesService {

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @CacheLogging
    public List<Coordinates> getAllCoordinates() {
        return coordinatesRepository.findAll();
    }
}