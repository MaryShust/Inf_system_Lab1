package infs.lab.controller.dto;

import infs.lab.db.entities.Location;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Локация")
public record LocationDTO(
        @Schema(description = "Локация X", example = "10.5") Double x,
        @Schema(description = "Локация Y", example = "20.5") Float y,
        @Schema(description = "Локация Z", example = "30.5") Double z
) {

    public static LocationDTO map(Location location) {
        if (location == null) {
            return new LocationDTO(
                    /* x = */ null,
                    /* y = */ null,
                    /* z = */ null
            );
        } else {
            return new LocationDTO(
                    location.getX(),
                    location.getY(),
                    location.getZ()
            );
        }
    }
}