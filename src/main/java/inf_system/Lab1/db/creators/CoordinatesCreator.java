package inf_system.Lab1.db.creators;

import inf_system.Lab1.db.entities.Coordinates;
import inf_system.Lab1.db.repositories.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CoordinatesCreator {

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Coordinates createCoordinates(int x, int y) {
        Coordinates result = coordinatesRepository.findByXAndYWithLock(x, y);
        if (result != null) {
            return result;
        } else {
            Coordinates coordinates = new Coordinates();
            coordinates.setX(x);
            coordinates.setY(y);
            return coordinatesRepository.save(coordinates);
        }
    }
}