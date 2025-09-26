package inf_system.Lab1.controller.dto;

import inf_system.Lab1.db.entities.Coordinates;

public class CoordinatesDTO {
    private Integer x;
    private Integer y;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public static CoordinatesDTO map(Coordinates coordinates) {
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
        coordinatesDTO.x = coordinates.getX();
        coordinatesDTO.y = coordinates.getY();
        return coordinatesDTO;
    }
}
