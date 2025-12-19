package infs.lab.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import infs.lab.controller.dto.CoordinatesDTO;
import infs.lab.controller.dto.LocationDTO;
import infs.lab.controller.dto.PersonDTO;
import infs.lab.controller.exception.ParsingException;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
public class FileParser {

    private final ObjectMapper objectMapper;

    public FileParser() {
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
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
            } else if ("txt".equalsIgnoreCase(fileExtension)) {
                return parseTxt(inputStream);
            } else {
                throw new ParsingException("Неподдерживаемый формат файла. Ожидается JSON или TXT.");
            }
        } catch (Exception e) {
            throw new ParsingException("Ошибка парсинга файла: " + e.getMessage());
        }
    }

    private List<PersonDTO> parseJson(InputStream inputStream) throws IOException {
        List<PersonDTO> people = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(inputStream);

            if (rootNode.isArray()) {
                for (JsonNode personNode : rootNode) {
                    PersonDTO person = parseJsonPerson(personNode);
                    if (person != null) {
                        people.add(person);
                    }
                }
            } else if (rootNode.isObject()) {
                PersonDTO person = parseJsonPerson(rootNode);
                if (person != null) {
                    people.add(person);
                }
            } else {
                throw new IllegalArgumentException("JSON должен содержать объект или массив объектов");
            }
        } catch (JsonProcessingException e) {
            throw new ParsingException("Ошибка парсинга JSON: " + e.getMessage());
        }

        return people;
    }

    private PersonDTO parseJsonPerson(JsonNode personNode) {
        long id = 0L;
        String name = "";
        CoordinatesDTO coordinatesDTO = null;
        LocalDate creationDate = null;
        String eyeColor = null;
        String hairColor = null;
        LocationDTO locationDTO = null;
        int height = 0;
        LocalDate birthday = null;
        String nationality = null;
        String photoId = null;

        try {
            if (personNode.has("id") && !personNode.get("id").isNull()) {
                JsonNode idNode = personNode.get("id");
                if (idNode.isNumber()) {
                    id = idNode.asLong();
                }
            }

            if (personNode.has("name") && !personNode.get("name").isNull()) {
                name = personNode.get("name").asText();
            }

            if (personNode.has("coordinates") && !personNode.get("coordinates").isNull()) {
                JsonNode coordsNode = personNode.get("coordinates");

                int x = 0;
                int y = 0;

                if (coordsNode.has("x") && !coordsNode.get("x").isNull()) {
                    JsonNode xNode = coordsNode.get("x");
                    if (xNode.isNumber()) {
                        x = xNode.asInt();
                    } else if (xNode.isTextual()) {
                        try {
                            x = Integer.parseInt(xNode.asText());
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле coordinates.x");
                        }
                    }
                }

                if (coordsNode.has("y") && !coordsNode.get("y").isNull()) {
                    JsonNode yNode = coordsNode.get("y");
                    if (yNode.isNumber()) {
                        y = yNode.asInt();
                    } else if (yNode.isTextual()) {
                        try {
                            y = Integer.parseInt(yNode.asText());
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле coordinates.y");
                        }
                    }
                }

                coordinatesDTO = new CoordinatesDTO(x, y);
            }

            if (personNode.has("eyeColor") && !personNode.get("eyeColor").isNull()) {
                eyeColor = personNode.get("eyeColor").asText();
            }

            if (personNode.has("hairColor") && !personNode.get("hairColor").isNull()) {
                hairColor = personNode.get("hairColor").asText();
            }

            if (personNode.has("location") && !personNode.get("location").isNull()) {
                JsonNode locationNode = personNode.get("location");
                double x = 0;
                float y = 0;
                double z = 0;

                if (locationNode.has("x") && !locationNode.get("x").isNull()) {
                    JsonNode xNode = locationNode.get("x");
                    if (xNode.isNumber()) {
                        x = xNode.asDouble();
                    } else if (xNode.isTextual()) {
                        try {
                            x = Double.parseDouble(xNode.asText());
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле location.x");
                        }
                    }
                }

                if (locationNode.has("y") && !locationNode.get("y").isNull()) {
                    JsonNode yNode = locationNode.get("y");
                    if (yNode.isNumber()) {
                        double yValue = yNode.asDouble();
                        y = (float) yValue;
                    } else if (yNode.isTextual()) {
                        try {
                            y = Float.parseFloat(yNode.asText());
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле location.y");
                        }
                    }
                }

                if (locationNode.has("z") && !locationNode.get("z").isNull()) {
                    JsonNode zNode = locationNode.get("z");
                    if (zNode.isNumber()) {
                        z = zNode.asDouble();
                    } else if (zNode.isTextual()) {
                        try {
                            z = Double.parseDouble(zNode.asText());
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле location.z");
                        }
                    }
                }

                locationDTO = new LocationDTO(x, y, z);
            }

            if (personNode.has("height") && !personNode.get("height").isNull()) {
                JsonNode heightNode = personNode.get("height");
                if (heightNode.isNumber()) {
                    height = heightNode.asInt();
                } else if (heightNode.isTextual()) {
                    try {
                        height = Integer.parseInt(heightNode.asText());
                    } catch (NumberFormatException e) {
                        throw new ParsingException("Не указано поле height");
                    }
                }
            }

            if (personNode.has("birthday") && !personNode.get("birthday").isNull()) {
                String birthdayStr = personNode.get("birthday").asText();
                if (birthdayStr != null && !birthdayStr.trim().isEmpty()) {
                    try {
                        birthday = LocalDate.parse(birthdayStr.trim());
                    } catch (DateTimeParseException e) {
                        throw new ParsingException("Не указано поле birthday");
                    }
                }
            }

            if (personNode.has("nationality") && !personNode.get("nationality").isNull()) {
                nationality = personNode.get("nationality").asText();
            }

            if (personNode.has("creationDate") && !personNode.get("creationDate").isNull()) {
                String creationDateStr = personNode.get("creationDate").asText();
                if (creationDateStr != null && !creationDateStr.trim().isEmpty()) {
                    try {
                        creationDate = LocalDate.parse(creationDateStr.trim());
                    } catch (DateTimeParseException e) {
                        throw new ParsingException("Не указано поле creationDate");
                    }
                }
            }

            if (personNode.has("photoId") && !personNode.get("photoId").isNull()) {
                photoId = personNode.get("photoId").asText();
            }

        } catch (Exception e) {
            throw new ParsingException("Ошибки парсинга");
        }

        return new PersonDTO(
                id,
                name,
                coordinatesDTO,
                eyeColor,
                hairColor,
                locationDTO,
                height,
                birthday,
                nationality,
                creationDate,
                photoId
        );
    }

    private List<PersonDTO> parseTxt(InputStream inputStream) throws IOException {
        List<PersonDTO> people = new ArrayList<>();
        Map<String, String> currentPerson = new HashMap<>();
        List<Map<String, String>> personDataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    if (!currentPerson.isEmpty()) {
                        personDataList.add(new HashMap<>(currentPerson));
                        currentPerson.clear();
                    }
                    continue;
                }

                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        currentPerson.put(key, value);
                    }
                }
            }

            if (!currentPerson.isEmpty()) {
                personDataList.add(new HashMap<>(currentPerson));
            }
        }

        for (Map<String, String> personData : personDataList) {
            try {
                PersonDTO person = parseTxtPerson(personData);
                if (person != null) {
                    people.add(person);
                }
            } catch (Exception e) {
                throw new ParsingException("Ошибки парсинга");
            }
        }

        return people;
    }

    private PersonDTO parseTxtPerson(Map<String, String> data) {
        long id = 0L;
        String name = "";
        CoordinatesDTO coordinatesDTO = null;
        LocalDate creationDate = null;
        String eyeColor = null;
        String hairColor = null;
        LocationDTO locationDTO = null;
        int height = 0;
        LocalDate birthday = null;
        String nationality = null;
        String photoId = null;

        try {
            if (data.containsKey("id") && !data.get("id").isEmpty()) {
                try {
                    id = Long.parseLong(data.get("id"));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле id");
                }
            }

            if (data.containsKey("name") && !data.get("name").isEmpty()) {
                name = data.get("name");
            }

            int coordinatesX = 0;
            int coordinatesY = 0;
            if (data.containsKey("coordinates.x") && !data.get("coordinates.x").isEmpty()) {
                try {
                    coordinatesX = Integer.parseInt(data.get("coordinates.x"));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле coordinates.x");
                }
            }
            if (data.containsKey("coordinates.y") && !data.get("coordinates.y").isEmpty()) {
                try {
                    coordinatesY = Integer.parseInt(data.get("coordinates.y"));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле coordinates.y");
                }
            }
            coordinatesDTO = new CoordinatesDTO(coordinatesX, coordinatesY);

            if (data.containsKey("eyeColor") && !data.get("eyeColor").isEmpty()) {
                eyeColor = data.get("eyeColor");
            }

            if (data.containsKey("hairColor") && !data.get("hairColor").isEmpty()) {
                hairColor = data.get("hairColor");
            }

            double locationX = 0;
            float locationY = 0;
            double locationZ = 0;
            if (data.containsKey("location.x") && !data.get("location.x").isEmpty()) {
                try {
                    locationX = Double.parseDouble(data.get("location.x"));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле location.x");
                }
            }
            if (data.containsKey("location.y") && !data.get("location.y").isEmpty()) {
                try {
                    locationY = Float.parseFloat(data.get("location.y"));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле location.y");
                }
            }
            if (data.containsKey("location.z") && !data.get("location.z").isEmpty()) {
                try {
                    locationZ = Double.parseDouble(data.get("location.z"));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле location.z");
                }
            }
            locationDTO = new LocationDTO(locationX, locationY, locationZ);

            if (data.containsKey("height") && !data.get("height").isEmpty()) {
                try {
                    height = Integer.parseInt(data.get("height"));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле height");
                }
            }

            if (data.containsKey("birthday") && !data.get("birthday").isEmpty()) {
                try {
                    birthday = LocalDate.parse(data.get("birthday"));
                } catch (Exception e) {
                    throw new ParsingException("Не указано поле birthday");
                }
            }

            if (data.containsKey("nationality") && !data.get("nationality").isEmpty()) {
                nationality = data.get("nationality");
            }

            if (data.containsKey("creationDate") && !data.get("creationDate").isEmpty()) {
                try {
                    creationDate = LocalDate.parse(data.get("creationDate"));
                } catch (DateTimeParseException e) {
                    throw new ParsingException("Не указано поле creationDate");
                }
            }

            if (data.containsKey("photoId") && !data.get("photoId").isEmpty()) {
                photoId = data.get("photoId");
            }

        } catch (Exception e) {
            throw new ParsingException("Ошибки парсинга");
        }

        return new PersonDTO(
                id,
                name,
                coordinatesDTO,
                eyeColor,
                hairColor,
                locationDTO,
                height,
                birthday,
                nationality,
                creationDate,
                photoId
        );
    }
}