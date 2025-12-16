package infs.lab.controller.dto;

import infs.lab.db.entities.History;
import infs.lab.services.MinioService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.text.DecimalFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "История")
public class HistoryDTO {

    @Schema(description = "Id истории")
    private Long id;

    @Schema(description = "Статус")
    private boolean status;

    @Schema(description = "Автор")
    private String author;

    @Schema(description = "Количество загруженных людей")
    private int countItems;

    @Schema(description = "Имя файла")
    private String originalFilename;

    @Schema(description = "Url для скачивания файла")
    private String downloadUrl;

    @Schema(description = "Размер файла")
    private Long fileSize;

    @Schema(description = "Отформатированный размер файла")
    private String formattedFileSize;

    public static HistoryDTO map(History history, MinioService minioService) {
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.id = history.getId();
        historyDTO.status = history.isStatus();
        historyDTO.author = history.getAuthor();
        historyDTO.countItems = history.getCountItems();
        historyDTO.originalFilename = history.getOriginalFilename();
        historyDTO.fileSize = history.getFileSize();
        historyDTO.formattedFileSize = formatFileSize(history.getFileSize());
        if (history.getFileObjectName() != null) {
            try {
                historyDTO.downloadUrl = minioService.getFileDownloadUrl(
                        history.getFileObjectName(),
                        history.getOriginalFilename()
                );
            } catch (Exception e) {
                historyDTO.downloadUrl = null;
            }
        }
        return historyDTO;
    }

    private static String formatFileSize(Long bytes) {
        if (bytes == null) return "N/A";
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        DecimalFormat df = new DecimalFormat("#,##0.#");
        return df.format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}