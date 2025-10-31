package inf_system.Lab1.services;

import inf_system.Lab1.db.creators.HistoryCreator;
import inf_system.Lab1.db.entities.History;
import inf_system.Lab1.db.repositories.HistoryRepository;
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

    @Transactional
    public void updateHistory(String userName, int countPersons) {
        historyCreator.createHistory(countPersons != 0, userName, countPersons);
    }

    @Transactional
    public List<History> getAllHistory(String author) {
        if (author.equals("admin")) {
            return historyRepository.findAll();
        } else {
            return historyRepository.findByAuthor(author);
        }
    }
}