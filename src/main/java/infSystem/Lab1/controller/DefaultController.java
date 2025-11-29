package infSystem.Lab1.controller;

import infSystem.Lab1.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String startPage(HttpServletRequest request) {
        if (authService.isUserAuthenticated(request)) {
            return "redirect:main";
        }
        return "redirect:auth";
    }
}