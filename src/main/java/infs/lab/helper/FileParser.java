package infs.lab.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import infs.lab.controller.dto.CoordinatesDTO;
import infs.lab.controller.dto.LocationDTO;
import infs.lab.controller.dto.PersonDTO;
import infs.lab.controller.exception.ParsingException;
import org.springframework.stereotype.Component;

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

    public List<PersonDTO> parseFileContent(String content) {
        try {
            return parseJson(content);
        } catch (Exception jsonException) {
            try {
                return parseTxt(content);
            } catch (Exception txtException) {
                throw new ParsingException("Неподдерживаемый формат файла. Ожидается JSON или TXT.");
            }
        }
    }

    private List<PersonDTO> parseJson(String content) throws Exception {
        List<PersonDTO> persons = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(content);

        if (rootNode.isArray()) {
            for (JsonNode personNode : rootNode) {
                PersonDTO person = parseJsonPerson(personNode);
                if (person != null) {
                    persons.add(person);
                }
            }
        } else if (rootNode.isObject()) {
            PersonDTO person = parseJsonPerson(rootNode);
            if (person != null) {
                persons.add(person);
            }
        } else {
            throw new IllegalArgumentException("JSON должен содержать объект или массив объектов");
        }

        return persons;
    }

    private PersonDTO parseJsonPerson(JsonNode personNode) {
        PersonDTO person = new PersonDTO();

        try {
            if (personNode.has("id") && !personNode.get("id").isNull()) {
                JsonNode idNode = personNode.get("id");
                if (idNode.isNumber()) {
                    person.setId(idNode.asLong());
                }
            }

            if (personNode.has("name") && !personNode.get("name").isNull()) {
                person.setName(personNode.get("name").asText());
            }

            if (personNode.has("coordinates") && !personNode.get("coordinates").isNull()) {
                JsonNode coordsNode = personNode.get("coordinates");
                CoordinatesDTO coordinates = new CoordinatesDTO();

                if (coordsNode.has("x") && !coordsNode.get("x").isNull()) {
                    JsonNode xNode = coordsNode.get("x");
                    if (xNode.isNumber()) {
                        coordinates.setX(xNode.asInt());
                    } else if (xNode.isTextual()) {
                        try {
                            coordinates.setX(Integer.parseInt(xNode.asText()));
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле coordinates.x");
                        }
                    }
                }

                if (coordsNode.has("y") && !coordsNode.get("y").isNull()) {
                    JsonNode yNode = coordsNode.get("y");
                    if (yNode.isNumber()) {
                        coordinates.setY(yNode.asInt());
                    } else if (yNode.isTextual()) {
                        try {
                            coordinates.setY(Integer.parseInt(yNode.asText()));
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле coordinates.y");
                        }
                    }
                }

                person.setCoordinates(coordinates);
            }

            if (personNode.has("eyeColor") && !personNode.get("eyeColor").isNull()) {
                String eyeColorStr = personNode.get("eyeColor").asText();
                person.setEyeColor(eyeColorStr);
            }

            if (personNode.has("hairColor") && !personNode.get("hairColor").isNull()) {
                String hairColorStr = personNode.get("hairColor").asText();
                person.setHairColor(hairColorStr);
            }

            if (personNode.has("location") && !personNode.get("location").isNull()) {
                JsonNode locationNode = personNode.get("location");
                LocationDTO location = new LocationDTO();

                if (locationNode.has("x") && !locationNode.get("x").isNull()) {
                    JsonNode xNode = locationNode.get("x");
                    if (xNode.isNumber()) {
                        location.setX(xNode.asDouble());
                    } else if (xNode.isTextual()) {
                        try {
                            location.setX(Double.parseDouble(xNode.asText()));
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле location.x");
                        }
                    }
                }

                if (locationNode.has("y") && !locationNode.get("y").isNull()) {
                    JsonNode yNode = locationNode.get("y");
                    if (yNode.isNumber()) {
                        double yValue = yNode.asDouble();
                        location.setY((float) yValue);
                    } else if (yNode.isTextual()) {
                        try {
                            location.setY(Float.parseFloat(yNode.asText()));
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле location.y");
                        }
                    }
                }

                if (locationNode.has("z") && !locationNode.get("z").isNull()) {
                    JsonNode zNode = locationNode.get("z");
                    if (zNode.isNumber()) {
                        location.setZ(zNode.asDouble());
                    } else if (zNode.isTextual()) {
                        try {
                            location.setZ(Double.parseDouble(zNode.asText()));
                        } catch (NumberFormatException e) {
                            throw new ParsingException("Не указано поле location.z");
                        }
                    }
                }

                person.setLocation(location);
            }

            if (personNode.has("height") && !personNode.get("height").isNull()) {
                JsonNode heightNode = personNode.get("height");
                if (heightNode.isNumber()) {
                    person.setHeight(heightNode.asInt());
                } else if (heightNode.isTextual()) {
                    try {
                        person.setHeight(Integer.parseInt(heightNode.asText()));
                    } catch (NumberFormatException e) {
                        throw new ParsingException("Не указано поле height");
                    }
                }
            }

            if (personNode.has("birthday") && !personNode.get("birthday").isNull()) {
                String birthdayStr = personNode.get("birthday").asText();
                if (birthdayStr != null && !birthdayStr.trim().isEmpty()) {
                    try {
                        person.setBirthday(Optional.of(birthdayStr));
                    } catch (DateTimeParseException e) {
                        throw new ParsingException("Не указано поле birthday");
                    }
                }
            }

            if (personNode.has("nationality") && !personNode.get("nationality").isNull()) {
                String nationalityStr = personNode.get("nationality").asText();
                person.setNationality(nationalityStr);
            }

            if (personNode.has("creationDate") && !personNode.get("creationDate").isNull()) {
                String creationDateStr = personNode.get("creationDate").asText();
                if (creationDateStr != null && !creationDateStr.trim().isEmpty()) {
                    try {
                        person.setCreationDate(LocalDate.parse(creationDateStr.trim()));
                    } catch (DateTimeParseException e) {
                        throw new ParsingException("Не указано поле creationDate");
                    }
                }
            }

            if (personNode.has("photoId") && !personNode.get("photoId").isNull()) {
                person.setPhotoId(personNode.get("photoId").asText());
            }

        } catch (Exception e) {
            throw new ParsingException("Ошибки парсинга");
        }

        return person;
    }

    private List<PersonDTO> parseTxt(String content) {
        List<PersonDTO> persons = new ArrayList<>();
        String[] lines = content.split("\n");

        Map<String, String> currentPerson = new HashMap<>();
        List<Map<String, String>> personDataList = new ArrayList<>();

        for (String line : lines) {
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

        for (Map<String, String> personData : personDataList) {
            try {
                PersonDTO person = parseTxtPerson(personData);
                if (person != null) {
                    persons.add(person);
                }
            } catch (Exception e) {
                throw new ParsingException("Ошибки парсинга");
            }
        }

        return persons;
    }

    private PersonDTO parseTxtPerson(Map<String, String> data) {
        PersonDTO person = new PersonDTO();

        try {
            if (data.containsKey("id") && !data.get("id").isEmpty()) {
                try {
                    person.setId(Long.parseLong(data.get("id")));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле id");
                }
            }

            if (data.containsKey("name") && !data.get("name").isEmpty()) {
                person.setName(data.get("name"));
            }

            CoordinatesDTO coordinates = new CoordinatesDTO();
            if (data.containsKey("coordinates.x") && !data.get("coordinates.x").isEmpty()) {
                try {
                    coordinates.setX(Integer.parseInt(data.get("coordinates.x")));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле coordinates.x");
                }
            }
            if (data.containsKey("coordinates.y") && !data.get("coordinates.y").isEmpty()) {
                try {
                    coordinates.setY(Integer.parseInt(data.get("coordinates.y")));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле coordinates.y");
                }
            }
            person.setCoordinates(coordinates);

            if (data.containsKey("eyeColor") && !data.get("eyeColor").isEmpty()) {
                person.setEyeColor(data.get("eyeColor"));
            }

            if (data.containsKey("hairColor") && !data.get("hairColor").isEmpty()) {
                person.setHairColor(data.get("hairColor"));
            }

            LocationDTO location = new LocationDTO();
            if (data.containsKey("location.x") && !data.get("location.x").isEmpty()) {
                try {
                    location.setX(Double.parseDouble(data.get("location.x")));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле location.x");
                }
            }
            if (data.containsKey("location.y") && !data.get("location.y").isEmpty()) {
                try {
                    location.setY(Float.parseFloat(data.get("location.y")));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле location.y");
                }
            }
            if (data.containsKey("location.z") && !data.get("location.z").isEmpty()) {
                try {
                    location.setZ(Double.parseDouble(data.get("location.z")));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле location.z");
                }
            }
            person.setLocation(location);

            if (data.containsKey("height") && !data.get("height").isEmpty()) {
                try {
                    person.setHeight(Integer.parseInt(data.get("height")));
                } catch (NumberFormatException e) {
                    throw new ParsingException("Не указано поле height");
                }
            }

            if (data.containsKey("birthday") && !data.get("birthday").isEmpty()) {
                try {
                    person.setBirthday(Optional.ofNullable(data.get("birthday")));
                } catch (Exception e) {
                    throw new ParsingException("Не указано поле birthday");
                }
            }

            if (data.containsKey("nationality") && !data.get("nationality").isEmpty()) {
                person.setNationality(data.get("nationality"));
            }

            if (data.containsKey("creationDate") && !data.get("creationDate").isEmpty()) {
                try {
                    person.setCreationDate(LocalDate.parse(data.get("creationDate")));
                } catch (DateTimeParseException e) {
                    throw new ParsingException("Не указано поле creationDate");
                }
            }

            if (data.containsKey("photoId") && !data.get("photoId").isEmpty()) {
                person.setPhotoId(data.get("photoId"));
            }

        } catch (Exception e) {
            throw new ParsingException("Ошибки парсинга");
        }

        return person;
    }
}