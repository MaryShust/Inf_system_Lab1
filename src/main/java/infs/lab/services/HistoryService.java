package infs.lab.services;

import infs.lab.config.AppConfig;
import infs.lab.db.creators.HistoryCreator;
import infs.lab.db.entities.History;
import infs.lab.db.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private HistoryCreator historyCreator;
    @Autowired
    private AppConfig appConfig;

    @Transactional
    public void updateHistory(String userName, int countPeople) {
        historyCreator.createHistory(countPeople != 0, userName, countPeople);
    }

    @Transactional
    public List<History> getAllHistory(String author) {
        if (author.equals(appConfig.getAdminName())) {
            return historyRepository.findAll();
        } else {
            return historyRepository.findByAuthor(author);
        }
    }
}