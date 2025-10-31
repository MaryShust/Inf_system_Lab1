package inf_system.Lab1.controller;

import inf_system.Lab1.db.entities.History;
import inf_system.Lab1.services.AuthService;
import inf_system.Lab1.services.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private AuthService authService;

    @GetMapping("/upload_history")
    public ResponseEntity<List<History>> getHistory(HttpServletRequest request) {
        String userName = authService.getUserName(request);
        List<History> history = historyService.getAllHistory(userName);
        return ResponseEntity.ok(history);
    }
}