package inf_system.Lab1.controller.dto;

import inf_system.Lab1.db.entities.Location;

public class LocationDTO {
    private Double x;
    private Float y;
    private Double z;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

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