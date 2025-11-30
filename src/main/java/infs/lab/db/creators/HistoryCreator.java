package infs.lab.db.creators;

import infs.lab.db.entities.History;
import infs.lab.db.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class HistoryCreator {

    @Autowired
    private HistoryRepository historyRepository;

    @Transactional
    public History createHistory(
            boolean status,
            String author,
            int countItems
    ) {
        History history = new History();
        history.setStatus(status);
        history.setAuthor(author);
        history.setCountItems(countItems);
        return historyRepository.save(history);
    }
}