package inf_system.Lab1.controller;

import inf_system.Lab1.helper.CookiesHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        CookiesHelper.deleteField(response, "userId");
        CookiesHelper.deleteField(response, "userName");
        return "redirect:/auth";
    }
}