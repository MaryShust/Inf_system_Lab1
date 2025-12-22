package infs.lab.services;

import infs.lab.aop.CacheLogging;
import infs.lab.config.AppConfig;
import infs.lab.controller.dto.HistoryDTO;
import infs.lab.db.creators.HistoryCreator;
import infs.lab.db.entities.History;
import infs.lab.db.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private HistoryCreator historyCreator;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private MinioService minioService;
    @Autowired
    private PersonService personService;

    @Transactional
    public void updateHistory(
            String userName,
            int countPeople,
            String originalFileName,
            String fileObjectName,
            Long fileSize
    ) {
        historyCreator.createHistory(
                countPeople != 0,
                userName,
                countPeople,
                originalFileName,
                fileObjectName,
                fileSize
        );
    }

    @CacheLogging
    @Transactional
    public List<HistoryDTO> getAllHistory(String author) {
        List<History> historyList;

        if (author.equals(appConfig.getAdminName())) {
            historyList = historyRepository.findAll();
        } else {
            historyList = historyRepository.findByAuthor(author);
        }

        return historyList.stream()
                .map(history -> HistoryDTO.map(history, minioService))
                .collect(Collectors.toList());
    }
}