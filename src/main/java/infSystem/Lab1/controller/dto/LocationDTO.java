package infSystem.Lab1.controller.dto;

import infSystem.Lab1.db.entities.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {

    private Double x;
    private Float y;
    private Double z;

    public static LocationDTO map(Location location) {
        LocationDTO locationDTO = new LocationDTO();
        if (location == null) {
            locationDTO.x = null;
            locationDTO.y = null;
            locationDTO.z = null;
        } else {
            locationDTO.x = location.getX();
            locationDTO.y = location.getY();
            locationDTO.z = location.getZ();
        }
        return locationDTO;
    }
}