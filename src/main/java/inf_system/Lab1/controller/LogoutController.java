package inf_system.Lab1.controller;

import inf_system.Lab1.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @Autowired
    private AuthService authService;

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        authService.clearAuthCookies(response);
        return "redirect:/auth";
    }
}