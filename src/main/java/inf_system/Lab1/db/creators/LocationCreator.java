package inf_system.Lab1.db.creators;

import inf_system.Lab1.db.entities.Location;
import inf_system.Lab1.db.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LocationCreator {

    @Autowired
    private LocationRepository locationRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Location createLocation(Double x, Float y, Double z) {
        if (x == null || y == null || z == null) {
            return null;
        }
        Location result = locationRepository.findByXAndYAndZWithLock(x, y, z);
        if (result != null) {
            return result;
        } else {
            Location location = new Location();
            location.setX(x);
            location.setY(y);
            location.setZ(z);
            return locationRepository.save(location);
        }
    }
}