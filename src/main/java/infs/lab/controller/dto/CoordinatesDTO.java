package infs.lab.controller.dto;

import infs.lab.db.entities.Coordinates;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Координаты")
public class CoordinatesDTO {

    @Schema(description = "Координата X", example = "100", maximum = "674")
    private Integer x;

    @Schema(description = "Координата Y", example = "-200", minimum = "-554")
    private Integer y;

    public static CoordinatesDTO map(Coordinates coordinates) {
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
        coordinatesDTO.x = coordinates.getX();
        coordinatesDTO.y = coordinates.getY();
        return coordinatesDTO;
    }
}