package infs.lab.controller.dto;

import infs.lab.db.entities.Coordinates;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Координаты")
public record CoordinatesDTO(
        @Schema(description = "Координата X", example = "100", maximum = "674") Integer x,
        @Schema(description = "Координата Y", example = "-200", minimum = "-554") Integer y
) {

    public static CoordinatesDTO map(Coordinates coordinates) {
        return new CoordinatesDTO(
                coordinates.getX(),
                coordinates.getY()
        );
    }
}