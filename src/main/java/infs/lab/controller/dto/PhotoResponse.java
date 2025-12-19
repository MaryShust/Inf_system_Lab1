package infs.lab.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с информацией о фотографии")
public record PhotoResponse(
        @Schema(description = "Сообщение о результате операции") String message,
        @Schema(description = "Идентификатор фотографии") String photoId,
        @Schema(description = "URL фотографии") String photoUrl
) {

}