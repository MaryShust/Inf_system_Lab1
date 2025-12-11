package infs.lab.controller.dto;

import infs.lab.db.entities.Location;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Локация")
public class LocationDTO {

    @Schema(description = "Локация X", example = "10.5")
    private Double x;

    @Schema(description = "Локация Y", example = "20.5")
    private Float y;

    @Schema(description = "Локация Z", example = "30.5")
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