package infs.lab.controller.dto;

import infs.lab.db.entities.History;
import infs.lab.services.MinioService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

@Schema(description = "История")
public record HistoryDTO(
        @Schema(description = "Id истории") Long id,
        @Schema(description = "Статус") boolean status,
        @Schema(description = "Автор") String author,
        @Schema(description = "Количество загруженных людей") int countItems,
        @Schema(description = "Имя файла") String originalFilename,
        @Schema(description = "Url для скачивания файла") String downloadUrl,
        @Schema(description = "Размер файла") Long fileSize,
        @Schema(description = "Отформатированный размер файла") String formattedFileSize
) {

    public static HistoryDTO map(History history, MinioService minioService) {
        String downloadUrl = null;
        if (history.getFileObjectName() != null) {
            try {
                downloadUrl = minioService.getFileDownloadUrl(
                        history.getFileObjectName(),
                        history.getOriginalFilename()
                );
            } catch (ServerException | InsufficientDataException |
                    ErrorResponseException | IOException |
                    NoSuchAlgorithmException | InvalidKeyException |
                    InvalidResponseException | XmlParserException |
                    InternalException ignored) {
                {}
            }
        }

        String formatFileSize;
        Long fileSize = history.getFileSize();
        if (fileSize == null) {
            formatFileSize = "N/A";
        } else {
            formatFileSize = formatFileSize(fileSize);
        }

        return new HistoryDTO(
                history.getId(),
                history.isStatus(),
                history.getAuthor(),
                history.getCountItems(),
                history.getOriginalFilename(),
                downloadUrl,
                fileSize,
                formatFileSize
        );
    }

    private static String formatFileSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        DecimalFormat df = new DecimalFormat("#,##0.#");
        return df.format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}