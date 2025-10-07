package inf_system.Lab1.controller;

import inf_system.Lab1.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/auth")
    public String authPage(HttpServletRequest request) {
        if (authService.isUserAuthenticated(request)) {
            return "redirect:main";
        }
        return "auth";
    }

    @PostMapping("/auth")
    public String auth(
            @RequestParam("username") String userName,
            @RequestParam("password") String password,
            HttpServletResponse response
    ) {
        authService.authenticateUser(userName, password, response);
        return "redirect:main";
    }
}