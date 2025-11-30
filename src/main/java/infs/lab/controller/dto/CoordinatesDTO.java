package infs.lab.controller.dto;

import infs.lab.db.entities.Coordinates;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordinatesDTO {

    private Integer x;
    private Integer y;

    public static CoordinatesDTO map(Coordinates coordinates) {
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
        coordinatesDTO.x = coordinates.getX();
        coordinatesDTO.y = coordinates.getY();
        return coordinatesDTO;
    }
}