package infs.lab.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import infs.lab.controller.dto.PersonDTO;
import infs.lab.controller.exception.ParsingException;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileParser {

    private final Gson gson;

    public FileParser() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    private String getExtension(String fileName) {
        if (fileName != null) {
            int lastDotIndex = fileName.lastIndexOf('.');
            if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
                return fileName.substring(lastDotIndex + 1).toLowerCase();
            }
        }
        return null;
    }

    public List<PersonDTO> parseFileContent(InputStream inputStream, String fileName) {
        try {
            String fileExtension = getExtension(fileName);
            if ("json".equalsIgnoreCase(fileExtension)) {
                return parseJson(inputStream);
            } else {
                throw new ParsingException("Неподдерживаемый формат файла. Ожидается JSON.");
            }
        } catch (Exception e) {
            throw new ParsingException("Ошибка парсинга файла: " + e.getMessage());
        }
    }

    private List<PersonDTO> parseJson(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (jsonElement.isJsonArray()) {
                // Парсим как массив объектов
                List<PersonDTO> result = new ArrayList<>();
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    PersonDTO person = gson.fromJson(element, PersonDTO.class);
                    result.add(person);
                }
                return result;
            } else if (jsonElement.isJsonObject()) {
                // Парсим как одиночный объект
                PersonDTO person = gson.fromJson(jsonElement, PersonDTO.class);
                List<PersonDTO> result = new ArrayList<>();
                result.add(person);
                return result;
            } else {
                throw new ParsingException("JSON должен содержать объект или массив объектов");
            }
        }
    }
}