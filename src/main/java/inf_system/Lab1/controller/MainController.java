package inf_system.Lab1.controller;

import inf_system.Lab1.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private AuthService authService;

    @GetMapping("/main")
    public String mainPage(
            Model model,
            HttpServletRequest request
    ) {
        if (!authService.isUserAuthenticated(request)) {
            return "redirect:auth";
        }
        String userId = authService.getUserId(request);
        String userName = authService.getUserName(request);
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        return "main";
    }
}